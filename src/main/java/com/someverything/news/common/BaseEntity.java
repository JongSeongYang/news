package com.someverything.news.common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity {

    /** 생성 일시 */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDt;

    /** 수정 일시 */
    @LastModifiedDate
    private LocalDateTime updatedDt;
}

