package com.someverything.news;

import com.someverything.news.domain.Member;
import com.someverything.news.domain.MemberDetail;
import com.someverything.news.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class NewsApplicationTests {

	@Autowired
	private MemberRepository memberRepository;

	@Test
	void contextLoads() {
		// Given
		// 회원을 미리 저장하고 id를 기억합니다.
		Member savedMember = memberRepository.save(createMember());
		Long memberId = savedMember.getId();

		// When
		Optional<Member> foundMemberOptional = memberRepository.findById(memberId);

		// Then
		Assertions.assertTrue(foundMemberOptional.isPresent());  // 회원이 존재하는지 확인
		Member foundMember = foundMemberOptional.get();  // Optional에서 실제 회원 객체를 가져옴
		Assertions.assertEquals(memberId, foundMember.getId());  // 조회한 회원의 id와 저장한 회원의 id 일치 여부 확인
		// ... (추가적인 검증 로직을 작성할 수 있습니다.)
	}


	// 새로운 Member 객체를 생성하는 메서드
	private Member createMember() {
		Member member = new Member();
		member.setNickname("JohnDoe");
		member.setPassword("password123");
		member.setJoinPathCode("001");
		member.setAccessToken("abc123");
		member.setTokenRenewDt(LocalDateTime.now());
		member.setIsEmailReceive("Y");
		member.setEmailReceiveDt(LocalDateTime.now());
		member.setIsSmsReceive("Y");
		member.setSmsReceiveDt(LocalDateTime.now());
		member.setStatusCode("01");
		member.setIsPwdInit("N");
		member.setPwdChangeDt(LocalDateTime.now());
		member.setLoginFailCnt(0);
		member.setLastLoginDt(LocalDateTime.now());
		member.setDeviceType("Desktop");
		member.setLoginIp("127.0.0.1");
		member.setDormantApplyDt(LocalDateTime.now());
		member.setTermsAgreement("Agreed");

		// MemberDetail 예시 설정
		MemberDetail memberDetail = new MemberDetail();
		memberDetail.setMemberNm("John Doe");
		memberDetail.setBirth("1990-01-01");
		memberDetail.setResidentNumber("123456-1234567");
		memberDetail.setTel("123-4567");
		memberDetail.setMobileNumber("987-6543");
		memberDetail.setGenderCode("M");
		memberDetail.setNationCode("KR");
		memberDetail.setInfoUpdateDt(LocalDateTime.now());

		member.setMemberDetail(memberDetail);

		return member;
	}

}
