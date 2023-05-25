package com.someverything.news.service;

import com.someverything.news.config.AppConfig;
import com.someverything.news.domain.Member;
import com.someverything.news.dto.MemberDto;
import com.someverything.news.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final AppConfig appConfig;

    @Transactional
    public Member createMember(MemberDto.MemberRequest request){
        Member member = appConfig.strictModelMapper().map(request, Member.class);
        return memberRepository.save(member);
    }
}
