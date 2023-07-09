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
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

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
    public Member updateMember(Long memberId, MemberDto.MemberRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomResponseStatusException(ExceptionCode.MEMBER_NOT_FOUND, ExceptionCode.MEMBER_NOT_FOUND.getMessage()));

        // 조건문 분기 (비밀번호가 null이 아니면 비밀번호 set)

        // 회원 정보 업데이트
        member.setNickname(request.getNickname());
        member.setIsEmailReceive(request.getIsEmailReceive());
        member.setIsSmsReceive(request.getIsSmsReceive());

        // 회원 상세 정보 업데이트
        MemberDetail memberDetail = member.getMemberDetail();
        MemberDetail updatedMemberDetail = appConfig.strictModelMapper().map(request, MemberDetail.class); // 기존의 MemberDetail 객체를 수정하게 되면 JPA에서 변경 감지(Dirty Checking)가 동작하지 않을 수 있음. 따라서  받아서 복사해주는 식으로 해야됨

        // 업데이트된 내용을 기존의 MemberDetail에 복사
        memberDetail.setMobileNumber(updatedMemberDetail.getMobileNumber());
        // +@


        // 회원 상세 정보와 회원 정보 간의 양방향 연관관계 설정
        memberDetail.setMember(member);

        // 업데이트된 회원 정보 저장
        memberRepository.save(member);

        // 업데이트된 회원 상세 정보 저장
        memberDetailRepository.save(memberDetail);

        return member;
    }

    @Override
    public String login(String nickname, String password) {
        // 사용자 인증 및 회원 정보 조회
        // nickname으로 찾아오는게 맞는지?
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new CustomResponseStatusException(ExceptionCode.MEMBER_NOT_FOUND, ExceptionCode.MEMBER_NOT_FOUND.getMessage()));

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomResponseStatusException(ExceptionCode.INVALID_PASSWORD, ExceptionCode.INVALID_PASSWORD.getMessage());
        }

        // 로그인 성공 시 토큰 발급
        String accessToken = jwtTokenProvider.createToken(member.getId(), "access_token", "member", 30); // 30분 유효한 액세스 토큰 생성

        return accessToken;
    }

    // 비밀번호 변경 기능 추가


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomResponseStatusException(ExceptionCode.MEMBER_NOT_FOUND, ExceptionCode.MEMBER_NOT_FOUND.getMessage()));

        // 얘는 어떻게 해야되지?
        // 회원 상태 코드를 삭제 상태로 변경
        member.setStatusCode("05"); // 삭제 상태 코드

        // 회원 상세 정보 삭제
        memberDetailRepository.deleteByMember(member);

        // 회원 삭제
        memberRepository.delete(member);



        // 상태 코드만 바꾸고 MemberQuit에 기록
    }
}
