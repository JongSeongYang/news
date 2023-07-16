package com.someverything.news.service;

import com.someverything.news.config.AppConfig;
import com.someverything.news.domain.AuthCode;
import com.someverything.news.domain.Member;
import com.someverything.news.domain.MemberDetail;
import com.someverything.news.domain.MemberQuit;
import com.someverything.news.dto.MemberDto;
import com.someverything.news.global.exception.CustomResponseStatusException;
import com.someverything.news.global.exception.ExceptionCode;
import com.someverything.news.global.utils.JwtTokenProvider;
import com.someverything.news.repository.AuthCodeRepository;
import com.someverything.news.repository.MemberDetailRepository;
import com.someverything.news.repository.MemberQuitRepository;
import com.someverything.news.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberDetailRepository memberDetailRepository;
    private final MemberQuitRepository memberQuitRepository;
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
    public String login(String email, String password) {
        // 사용자 인증 및 회원 정보 조회
        Member member = memberRepository.findByMemberDetailEmail(email)
                .orElseThrow(() -> new CustomResponseStatusException(ExceptionCode.MEMBER_NOT_FOUND, ExceptionCode.MEMBER_NOT_FOUND.getMessage()));

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomResponseStatusException(ExceptionCode.INVALID_PASSWORD, ExceptionCode.INVALID_PASSWORD.getMessage());
        }

        // 로그인 성공 시 토큰 발급
        String accessToken = jwtTokenProvider.createToken(member.getId(), "access_token", "member", 30); // 30분 유효한 액세스 토큰 생성

        return accessToken;
    }

    @Override
    public void changePassword(Long memberId, String currentPassword, String newPassword) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomResponseStatusException(ExceptionCode.MEMBER_NOT_FOUND, ExceptionCode.MEMBER_NOT_FOUND.getMessage()));

        // 현재 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
            throw new CustomResponseStatusException(ExceptionCode.INVALID_PASSWORD, ExceptionCode.INVALID_PASSWORD.getMessage());
        }

        // 비밀번호 변경
        String encodedPassword = passwordEncoder.encode(newPassword);
        member.setPassword(encodedPassword);

        // 비밀번호 변경 일시 업데이트
        member.setPwdChangeDt(LocalDateTime.now());

        // 회원 정보 저장
        memberRepository.save(member);
    }

    @Override
    public void initPassword(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomResponseStatusException(ExceptionCode.MEMBER_NOT_FOUND, ExceptionCode.MEMBER_NOT_FOUND.getMessage()));

        // 새로운 임시 비밀번호 생성
        String newPassword = generateRandomPassword();

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(newPassword);

        // 비밀번호 업데이트
        member.setPassword(encodedPassword);
        // 비밀번호 초기화 여부 업데이트
        if ("Y".equals(member.getIsPwdInit())) {
            member.setIsPwdInit("N");
        }
        // 비밀번호 변경 일시 업데이트
        member.setPwdChangeDt(LocalDateTime.now());

        // 회원 정보 저장
        memberRepository.save(member);

        // 초기화된 비밀번호 전송 등의 추가 로직 작성 가능
    }

    private String generateRandomPassword() {
        // 임시 비밀번호 생성 로직 작성
        // 적절한 방식으로 무작위 비밀번호를 생성하여 반환: 임시 비밀번호는 8자리의 영문 대소문자와 숫자로 구성된 랜덤 문자열
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteMember(Long memberId, String quitReasonCode, String quitContent) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomResponseStatusException(ExceptionCode.MEMBER_NOT_FOUND, ExceptionCode.MEMBER_NOT_FOUND.getMessage()));

        // 회원 상태 코드를 탈퇴 상태로 변경
        member.setStatusCode("50"); // 탈퇴 상태 코드

        // 회원 탈퇴 기록 저장
        MemberQuit memberQuit = MemberQuit.builder()
                .id(memberId)
                .quitReasonCode(quitReasonCode)
                .quitContent(quitContent)
                .rejoinDt(null) // 재가입 가능 일자는 null로 설정
                .deleteDt(null) // 개인정보 파기 일자는 null로 설정
                .build();
        memberQuitRepository.save(memberQuit);

        // 회원 업데이트
        memberRepository.save(member);
    }
}
