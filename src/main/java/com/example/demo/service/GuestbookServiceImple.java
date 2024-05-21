package com.example.demo.service;

import com.example.demo.dto.GuestbookDTO;
import com.example.demo.dto.PageRequestDTO;
import com.example.demo.dto.PageResultDTO;
import com.example.demo.entity.Guestbook;
import com.example.demo.repository.GuestbookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO){

        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        Page<Guestbook> result = repository.findAll(pageable);

        Function<Guestbook, GuestbookDTO> fn = (entity -> entityTODTO(entity));

        return new PageResultDTO<>(result, fn);
    }
}
