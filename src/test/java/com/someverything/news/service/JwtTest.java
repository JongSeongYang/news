package com.someverything.news.service;

import com.someverything.news.domain.Member;
import com.someverything.news.global.utils.JwtTokenProvider;
import com.someverything.news.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class JwtTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void testToken생성(){
        // given
        String token = jwtTokenProvider.createToken(1L, "user", "inquiry", 3);
        // when
        // then
        log.info(token);
        assertNotEquals(token,"");
    }

    @Test
    void testTokenValidate_3일후만료(){
        // given
        String token = jwtTokenProvider.createToken(1L, "user", "inquiry", 3);
        log.info("token : " + token);
        // when
        boolean result = jwtTokenProvider.validateToken(token);
        // then
        assertTrue(result);
    }

    @Test
    void testTokenValidate_1일전만료(){
        // given
        String token = jwtTokenProvider.createToken(1L, "user", "inquiry", -1);
        log.info("token : " + token);
        // when
        boolean result = jwtTokenProvider.validateToken(token);
        // then
        assertFalse(result);
    }

    @Test
    void testTokeMemberId추출(){
        // given
        String token = jwtTokenProvider.createToken(1L, "user", "inquiry", 3);
        log.info("token : " + token);
        // when
        Long memberId = jwtTokenProvider.getMemberId(token);
        // then
        assertEquals(memberId,1L);
    }
}
