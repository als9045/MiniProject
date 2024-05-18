package com.example.demo.service;

import com.example.demo.dto.GuestbookDTO;
import com.example.demo.entity.Guestbook;
import com.example.demo.repository.GuestbookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class GuestbookSErviceImple implements  GuestbookService{

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

}
