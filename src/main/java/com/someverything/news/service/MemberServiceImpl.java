package com.someverything.news.service;

import com.someverything.news.config.AppConfig;
import com.someverything.news.domain.AuthCode;
import com.someverything.news.domain.Member;
import com.someverything.news.domain.MemberDetail;
import com.someverything.news.dto.MemberDto;
import com.someverything.news.global.utils.JwtTokenProvider;
import com.someverything.news.repository.AuthCodeRepository;
import com.someverything.news.repository.MemberDetailRepository;
import com.someverything.news.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberDetailRepository memberDetailRepository;
    private final AppConfig appConfig;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Member createMember(MemberDto.MemberRequest request){
        Member member = appConfig.strictModelMapper().map(request, Member.class);

        // PasswordEncoder 넣기

        memberRepository.save(member);

        MemberDetail memberDetail = appConfig.strictModelMapper().map(request, MemberDetail.class);
        memberDetail.setMember(member); // MemberDetail 엔티티 관계 설정
        memberDetailRepository.save(memberDetail);

        return member;
    }

    @Override
    public Member getMember(Long memberId) {
        return null;
    }

    @Override
    public void deleteMember(Long memberId) {

    }
}
