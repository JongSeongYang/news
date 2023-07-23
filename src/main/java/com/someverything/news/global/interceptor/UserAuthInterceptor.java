package com.someverything.news.global.interceptor;

import com.someverything.news.global.annotation.Authenticate;
import com.someverything.news.global.exception.CustomResponseStatusException;
import com.someverything.news.global.exception.ExceptionCode;
import com.someverything.news.global.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserAuthInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION = "Authorization";

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }
        Authenticate authenticate = ((HandlerMethod) handler).getMethodAnnotation(Authenticate.class);
        if(null != authenticate) {

            String token = extract(request, "Bearer");

            // 토큰이 없을 경우
            if (token.equals("")) {
                log.error("Interceptor >> UNAUTHORIZED(Empty Token)");
                throw new CustomResponseStatusException(ExceptionCode.UNAUTHORIZED, "");
            }

            if (!jwtTokenProvider.validateToken(token)) {
                log.error("Interceptor >> INVALID_TOKEN");
                throw new CustomResponseStatusException(ExceptionCode.INVALID_TOKEN, "");
            }

            Map<String, String> map = jwtTokenProvider.getClaims(token);
            request.setAttribute("id", Long.parseLong(map.get("id")));
            request.setAttribute("type", map.get("type"));
            request.setAttribute("scope", map.get("scope"));
        }
        return true;
    }

    private String extract(HttpServletRequest request, String type) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if (value.startsWith(type)) {
                return value.substring(type.length()).trim();
            }
        }
        return "";
    }


}
