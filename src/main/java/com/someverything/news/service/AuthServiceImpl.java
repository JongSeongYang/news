package com.someverything.news.service;

import com.someverything.news.dto.AuthDto;
import com.someverything.news.global.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthDto.AuthResponse createToken(AuthDto.AuthRequest request) {
        String token = jwtTokenProvider.createToken(request.getId(), request.getType(), request.getScope(), request.getExpire());

        return AuthDto.AuthResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public AuthDto.tokenCheckResponse checkToken(HttpServletRequest request) {
        String token = jwtTokenProvider.getTokenByHeader(request);
        Map<String, String> claims = jwtTokenProvider.getClaims(token);
        return AuthDto.tokenCheckResponse.builder()
                .id((Long) request.getAttribute("id"))
                .scope((String) request.getAttribute("scope"))
                .type((String) request.getAttribute("type"))
                .build();
    }
}
