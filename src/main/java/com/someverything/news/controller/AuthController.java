package com.someverything.news.controller;

import com.someverything.news.domain.Member;
import com.someverything.news.dto.AuthDto;
import com.someverything.news.global.annotation.Authenticate;
import com.someverything.news.global.exception.CustomResponseStatusException;
import com.someverything.news.global.exception.ExceptionCode;
import com.someverything.news.service.AuthServiceImpl;
import com.someverything.news.service.MemberService;
import com.someverything.news.service.MemberServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;
    
    @ApiOperation(value = "토큰생성", notes = "토큰생성")
    @PostMapping(value = "/token/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthDto.AuthResponse> signUp(@RequestBody AuthDto.AuthRequest authRequest,
                                                       HttpServletRequest request) {
        AuthDto.AuthResponse response = authService.createToken(authRequest);
        return ResponseEntity.ok(response);
    }

    @Authenticate
    @ApiOperation(value = "Token 유효 확인", notes = "Token 유효 확인")
    @PostMapping(value = "/token/verify", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthDto.tokenCheckResponse> tokenCheck(HttpServletRequest request) {
        AuthDto.tokenCheckResponse response = authService.checkToken(request);
        return ResponseEntity.ok(response);
    }
}
