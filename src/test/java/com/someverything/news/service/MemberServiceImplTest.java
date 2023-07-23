package com.someverything.news.service;

import com.someverything.news.domain.Member;
import com.someverything.news.dto.MemberDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
class MemberServiceImplTest {
    @Autowired
    private MemberService memberService;

    @Test
    public void testCreateMember() {
        // given
        MemberDto.MemberRequest request = MemberDto.MemberRequest.builder()
                .nickname("JohnDoe")
                .accessToken("abcde12345")
                .joinPathCode("01")
                .isEmailReceive("Y")
                .isSmsReceive("Y")
                .statusCode("01")
                .password("password123")
                .build();

        // when
        Member createdMember = memberService.createMember(request);

        // then
        assertNotNull(createdMember.getId());
        assertEquals("JohnDoe", createdMember.getNickname());
    }
}
