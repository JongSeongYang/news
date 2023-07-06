package com.someverything.news.repository;

import com.someverything.news.domain.Member;
import com.someverything.news.domain.MemberDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberDetailRepository extends JpaRepository<MemberDetail, Long> {

    void deleteByMember(Member member);
}
