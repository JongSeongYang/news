package com.someverything.news.repository;

import com.someverything.news.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void clear() {
        memberRepository.deleteAll();
    }

    @Test
    void Member생성(){

        Member me = Member.builder()
                .nickname("석맨")
                .password("1234")
                .statusCode("01")
                .build();
        Member save = memberRepository.save(me);
        assertEquals(save.getId(),1);
    }

    @Test
    void Member조회(){

        Member me = Member.builder()
                .nickname("석맨")
                .password("1234")
                .statusCode("01")
                .build();
        Member save = memberRepository.save(me);
        assertEquals(save.getId(),1);
    }

    @Test
    void Member수정() {
        Member me = Member.builder()
                .nickname("석맨")
                .password("1234")
                .statusCode("01")
                .build();
        Member savedMember = memberRepository.save(me);

        savedMember.setNickname("종성");
        savedMember.setPassword("5678");
        savedMember.setStatusCode("02");
        memberRepository.save(savedMember);

        Member updatedMember = memberRepository.findById(savedMember.getId()).orElse(null);
        assertEquals("종성", updatedMember.getNickname());
        assertEquals("5678", updatedMember.getPassword());
        assertEquals("02", updatedMember.getStatusCode());
    }

    @Test
    void Member삭제() {
        Member me = Member.builder()
                .nickname("석맨")
                .password("1234")
                .statusCode("01")
                .build();
        Member savedMember = memberRepository.save(me);
        memberRepository.delete(savedMember);

        assertNull(memberRepository.findById(savedMember.getId()).orElse(null));
    }
}