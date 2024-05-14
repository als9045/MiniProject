package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@MappedSuperclass //부모 클래스로 공통된 매핑정보를 정의
@EntityListeners(value = {AuditingEntityListener.class}) //생성이나 변경등의 이벤트 처리
@Getter
public class baseEntity {

    @CreatedDate //엔티티가 생성될때 생성일 자동 저장
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regdate;

    @LastModifiedDate //수정될때 자동으로 수정일 저장
    @Column(name="moddate")
    private LocalDateTime modDate;
}
