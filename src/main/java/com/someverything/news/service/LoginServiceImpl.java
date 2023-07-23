package com.someverything.news.service;

import com.someverything.news.domain.Member;
import com.someverything.news.global.exception.CustomResponseStatusException;
import com.someverything.news.global.exception.ExceptionCode;
import com.someverything.news.global.utils.HashUtils;
import com.someverything.news.global.utils.JwtTokenProvider;
import com.someverything.news.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService{
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final HashUtils hashUtils;

    @Transactional(noRollbackFor = CustomResponseStatusException.class)
    @Override
    public String login(String email, String password) {
        // 사용자 인증 및 회원 정보 조회
        Member member = memberRepository.findByMemberDetailEmail(email)
                .orElseThrow(() -> new CustomResponseStatusException(ExceptionCode.MEMBER_NOT_FOUND, ""));
        // 비밀번호 5회 이상 틀려서 잠긴 계정인지 확인
        if (member.getStatusCode().equals("20")) {
            throw new CustomResponseStatusException(ExceptionCode.LOCK_MEMBER, "");
        }
        // 비밀번호 일치 여부 확인
        if (!password.equals(hashUtils.toPasswordHash(member.getPassword()))) {
            int wrongCnt = member.getLoginFailCnt();
            member.setLoginFailCnt(wrongCnt + 1);
            // 비밀번호 5회 이상 틀렸는지 확인
            if (wrongCnt >= 5) {
                member.setStatusCode("20");
                throw new CustomResponseStatusException(ExceptionCode.WRONG_PASSWORD_OVER_FIVE, "");
            }
            throw new CustomResponseStatusException(ExceptionCode.INVALID_PASSWORD, "");
        }
        // 로그인 성공 시 토큰 발급
        String accessToken = jwtTokenProvider.createToken(member.getId(), "access_token", "member", 30); // 30분 유효한 액세스 토큰 생성
        // 비밀번호 틀린 횟수 초기화
        member.setLoginFailCnt(0);
        return accessToken;
    }

    @Override
    public String autoLogin(String token) {
        // 토큰 유효성 검증
        if (!jwtTokenProvider.validateToken(token)) {
            throw new CustomResponseStatusException(ExceptionCode.INVALID_TOKEN, ExceptionCode.INVALID_TOKEN.getMessage());
            // 토큰이 유효하지 않으면 로그아웃
        }

        // 토큰이 유효하고 유효기간이 남아있는 경우, 자동 로그인 처리
        Long memberId = jwtTokenProvider.getMemberId(token);
        // 회원 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomResponseStatusException(ExceptionCode.MEMBER_NOT_FOUND, ExceptionCode.MEMBER_NOT_FOUND.getMessage()));

        // 최종 로그인 일시 업데이트
        member.setLastLoginDt(LocalDateTime.now());

        // 새로운 토큰 발급
        String newAccessToken = jwtTokenProvider.createToken(member.getId(), "access_token", "member", 30); // 30분 유효한 새로운 액세스 토큰 생성
        member.setAccessToken(newAccessToken);

        // 회원 정보 저장
        memberRepository.save(member);

        return newAccessToken;
    }
}
