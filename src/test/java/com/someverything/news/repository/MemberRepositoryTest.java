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
}