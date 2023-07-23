package com.someverything.news.dto;

import lombok.*;

import java.time.LocalDateTime;

public class AuthDto {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class AuthRequest {
        private Long id;
        private String type;
        private String scope;
        private Integer expire;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class AuthResponse {
        private String token;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class tokenCheckResponse {
        private Long id;
        private String type;
        private String scope;
        private Integer expire;
    }

}
