package com.someverything.news.service;

public interface LoginService {
    String login(String username, String password);
    String autoLogin(String token);
}
