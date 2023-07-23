package com.someverything.news.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.OK;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    /* 10000  */
    EMAIL_NOT_FOUND(10001,"이메일이 존재하지 않습니다."),
    INVALID_TOKEN(10002,"유효하지 않은 토큰입니다."),
    MEMBER_NOT_FOUND(10003, "존재하지 않는 회원입니다."),
    INVALID_PASSWORD(10004, "비밀번호가 일치하지 않습니다."),
    WRONG_PASSWORD_OVER_FIVE(100005, "비밀번호를 5회 이상 틀렸습니다."),
    LOCK_MEMBER(100006, "잠긴 계정입니다."),
    UNAUTHORIZED(100007, "권한이 없습니다."),
    /* 500 internal server error */
    INTERNAL_ERROR(500, "Internal server error")
    ;

    private final int code;
    private final String message;
    private final HttpStatus status = OK;
}
