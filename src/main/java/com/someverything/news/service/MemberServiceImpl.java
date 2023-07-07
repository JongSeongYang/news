package com.someverything.news.service;

import com.someverything.news.config.AppConfig;
import com.someverything.news.domain.AuthCode;
import com.someverything.news.domain.Member;
import com.someverything.news.domain.MemberDetail;
import com.someverything.news.dto.MemberDto;
import com.someverything.news.global.exception.CustomResponseStatusException;
import com.someverything.news.global.exception.ExceptionCode;
import com.someverything.news.global.utils.JwtTokenProvider;
import com.someverything.news.repository.AuthCodeRepository;
import com.someverything.news.repository.MemberDetailRepository;
import com.someverything.news.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

        String encodedPassword = appConfig.passwordEncoder().encode(request.getPassword());
        member.setPassword(encodedPassword);
        member.setIsSmsReceive(request.getIsSmsReceive());
        member.setIsEmailReceive(request.getIsEmailReceive());

        memberRepository.save(member);

        MemberDetail memberDetail = appConfig.strictModelMapper().map(request, MemberDetail.class);
        memberDetail.setMember(member); // MemberDetail 엔티티 관계 설정
        memberDetailRepository.save(memberDetail);

        return member;
    }

    @Override
    public Member getMember(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        return optionalMember.orElseThrow(() -> new CustomResponseStatusException(ExceptionCode.MEMBER_NOT_FOUND, ExceptionCode.MEMBER_NOT_FOUND.getMessage()));
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteMember(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElseThrow(() -> new CustomResponseStatusException(ExceptionCode.MEMBER_NOT_FOUND, ExceptionCode.MEMBER_NOT_FOUND.getMessage()));


        // 얘는 어떻게 해야되지?
        // 회원 상태 코드를 삭제 상태로 변경
        member.setStatusCode("05"); // 삭제 상태 코드

        // 회원 상세 정보 삭제
        memberDetailRepository.deleteByMember(member);

        // 회원 삭제
        memberRepository.delete(member);
    }
}
