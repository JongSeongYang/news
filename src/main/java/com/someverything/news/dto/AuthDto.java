package com.someverything.news.dto;

import lombok.*;

import java.time.LocalDateTime;

public class AuthDto {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class AuthRequest {
        private String nickname;
    }



}
