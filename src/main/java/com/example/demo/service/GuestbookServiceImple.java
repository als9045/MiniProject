package com.example.demo.service;

import com.example.demo.dto.GuestbookDTO;
import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResultDTO;
import com.example.demo.entity.Guestbook;
import com.example.demo.entity.QGuestbook;
import com.example.demo.repository.GuestbookRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor //의존성 자동 주입
public class GuestbookServiceImple implements  GuestbookService{

    private final GuestbookRepository repository; //반드시 final로 선언


    @Override
    public Long register(GuestbookDTO dto) {

        log.info("DTO------------");
        log.info(dto);

        Guestbook entity = dtoTOEntity(dto);

        log.info(entity);

        repository.save(entity);

        return entity.getGno();
    }

    @Override
    public void remove(Long gno) {
        repository.deleteById(gno);
    }

    @Override
    public void modify(GuestbookDTO dto) {

        //업데이트 하는 항목은 제목, 내용
    Optional<Guestbook> result = repository.findById(dto.getGno());
        if(result.isPresent()){

            Guestbook entity = result.get();

            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());

            repository.save(entity);
        }
    }

    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO){

        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        BooleanBuilder builder = getSearch(requestDTO);

        Page<Guestbook> result = repository.findAll(builder, pageable);

        Function<Guestbook, GuestbookDTO> fn = (entity -> entityTODTO(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public GuestbookDTO read(Long gno) {

        Optional<Guestbook> result = repository.findById(gno);
        return result.isPresent()? entityTODTO((result.get())) : null;
    }

    private BooleanBuilder getSearch(PageRequestDTO requestDTO){
        String type = requestDTO.getType();


        //BooleanBuilder는 쿼리 조건을 저장하기 위해 사용
        BooleanBuilder builder = new BooleanBuilder();

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = requestDTO.getKeyword();

        //BooleanExpression Querydsl에서 사용하는 조건 표현식
        //gt(0L)은 greater than (크다) 조건을 의미
        BooleanExpression expression = qGuestbook.gno.gt(0L); // gno > 0

        builder.and(expression);

        if(type == null || type.trim().length() == 0){
            return builder;
        }

        BooleanBuilder condition = new BooleanBuilder();

        if(type.contains("t")){
            condition.or(qGuestbook.title.contains(keyword));
        }
        if(type.contains("c")){
            condition.or(qGuestbook.content.contains(keyword));
        }
        if(type.contains("w")){
            condition.or(qGuestbook.writer.contains(keyword));
        }

        //모든조건 통합
        builder.and(condition);

        return builder;

    }
}
