package com.someverything.news.service;

import com.someverything.news.dto.AuthDto;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    AuthDto.AuthResponse createToken(AuthDto.AuthRequest request);
    AuthDto.tokenCheckResponse checkToken(HttpServletRequest request);
}
