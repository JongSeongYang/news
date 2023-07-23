package com.someverything.news.repository;

import com.someverything.news.domain.AuthCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthCodeRepository extends JpaRepository<AuthCode, Long> {

}
