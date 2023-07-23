package com.someverything.news.dto;

import lombok.*;

import java.time.LocalDateTime;

public class MemberDto {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class MemberRequest {
        private String nickname;
        /** 토큰 */
        private String accessToken;
        /** 토큰 갱신 일시 */
        private LocalDateTime tokenRenewDt;
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
        /** 회원 상태 코드(01 : 정상, 02 : 비밀번호 오류 5회(대기?), 03 : 휴면, 04: 재가입, 05: ) */
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

        /** 회원 명 */
        private String memberNm;

        /** 생년월일 */
        private String birth;

        /** 주민번호 */
        private String residentNumber;

        /** 전화 */
        private String tel;

        /** 휴대폰 */
        private String mobileNumber;

        /** 이메일 */
        private String email;

        /** 성별 구분 코드 */
        private String genderCode;

        /** 국적 구분 코드 */
        private String nationCode;

        /** 개인정보 수정 일시 */
        private LocalDateTime infoUpdateDt;
    }



}
