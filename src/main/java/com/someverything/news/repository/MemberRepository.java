package com.someverything.news.repository;

import com.someverything.news.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long id);

    // Optional<Member> findByNickname(String nickname);
    // List<Member> findAllByStatusCode(String statusCode);
}
