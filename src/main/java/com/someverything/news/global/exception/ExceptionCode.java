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
    INVALID_PASSWORD(1000, "비밀번호가 일치하지 않습니다."),
    /* 500 internal server error */
    INTERNAL_ERROR(500, "Internal server error")
    ;

    private final int code;
    private final String message;
    private final HttpStatus status = OK;
}
