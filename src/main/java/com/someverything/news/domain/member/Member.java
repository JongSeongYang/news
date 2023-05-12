package com.someverything.news.domain.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Member")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Member {
    /** 회원 번호(PK) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 닉네임 */
    private String nickname;

    /** 토큰 */
    private String accessToken;

    /** 토큰 갱신 일시 */
    private LocalDateTime tokenRenewDt;

    /** 가입 일시 */
    private LocalDateTime joinDt;

    /** 가입 경로 코드 */
    private String joinPathCode;

    /** 이메일 수신 여부 */
    private String isEmailReceive;

    /** 이메일 수신 동의/거부 일시 */
    private LocalDateTime emailReceiveDt;

    /** SMS 수신 여부 */
    private String isSmsReceive;

    /** SMS 수신 동의/거부 일시 */
    private LocalDateTime smsReceiveDt;

    /** 회원 상태 코드(01 : 정상, 02 : 비밀번호 오류 5회, 03 : 휴면, 04: 재가입, 05: ) */
    private String statusCode;

    /** 비밀번호 */
    private String password;

    /** 비밀번호 초기화 여부 */
    private String isPwdInit;

    /** 비밀번호 변경 일시 */
    private LocalDateTime pwdChangeDt;

    /** 로그인 실패 횟수 */
    private Integer loginFailCnt;

    /** 최종 로그인 일시 */
    private LocalDateTime lastLoginDt;

    /** 접근 디바이스 종류 */
    private String deviceType;

    /** 접근 IP */
    private String loginIp;

    /** 휴면 적용 일시 */
    private LocalDateTime dormantApplyDt;

    /** 약관 동의 항목 */
    private String termsAgreement;

    @OneToOne(mappedBy = "member")
    private MemberDetail memberDetail;
}
