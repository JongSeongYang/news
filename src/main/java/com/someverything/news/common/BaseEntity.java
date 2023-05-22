package com.someverything.news.common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity {

    /** 생성 일시 */
//    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDt;

    /** 수정 일시 */
//    @LastModifiedDate
    private LocalDateTime updatedDt;

    @PrePersist
    public void onPrePersist() {
        this.createdDt = LocalDateTime.now(ZoneOffset.UTC);
        this.updatedDt = this.createdDt;
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedDt = LocalDateTime.now(ZoneOffset.UTC);
    }
}

