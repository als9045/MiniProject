package com.example.demo.repository;

import com.example.demo.entity.Guestbook;
import com.example.demo.entity.QGuestbook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    public void insertDimmies() {
        IntStream.rangeClosed(1, 300).forEach(i -> {
            Guestbook guestbook = Guestbook.builder()
                    .title("title....." + i)
                    .content("content...." + i)
                    .writer("user" + (i % 10))
                    .build();
            System.out.println(guestbookRepository.save(guestbook));


        });
    }

    @Test
    public void updateTest() {
        Optional<Guestbook> result = guestbookRepository.findById(300L);

        if (result.isPresent()) {

            Guestbook guestbook = result.get();

            guestbook.changeTitle("changed title...");
            guestbook.changeContent("changed content...");
            guestbookRepository.save(guestbook);
        }
    }

    @Test
    //단일 항목
    public void testQuery1() {

        // 첫 번째 페이지(0번 인덱스)부터 10개의 데이터를 가져오며, gno 필드를 기준으로 내림차순으로 정렬됩니다.
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        //Querydsl을 통한 동적 쿼리 생성
        BooleanBuilder builder = new BooleanBuilder();
        BooleanExpression expression = qGuestbook.title.contains(keyword);
        builder.and(expression);

        //Repository를 통한 쿼리 실행
        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);
        result.stream().forEach(guestbook -> {
            result.stream().forEach(System.out::println);
        });
    }

    @Test
    //다중 항목 검색
    public void testQuery2() {

        // 첫 번째 페이지(0번 인덱스)부터 10개의 데이터를 가져오며, gno 필드를 기준으로 내림차순으로 정렬됩니다.
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        //Querydsl을 통한 동적 쿼리 생성
        BooleanBuilder builder = new BooleanBuilder();
        BooleanExpression exTitle = qGuestbook.title.contains(keyword);
        BooleanExpression exContent = qGuestbook.content.contains(keyword);
        BooleanExpression exAll = exTitle.or(exContent);
        builder.and(exAll);
        builder.and(qGuestbook.gno.gt(0L));


        //Repository를 통한 쿼리 실행
        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);
        result.forEach(System.out::println);
    }
}
