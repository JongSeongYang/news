package com.someverything.news.repository;

import com.someverything.news.domain.MemberQuit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberQuitRepository extends JpaRepository<MemberQuit, Long> {
}
