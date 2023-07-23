package com.someverything.news.global.utils;

import com.someverything.news.global.exception.CustomResponseStatusException;
import com.someverything.news.global.exception.ExceptionCode;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.key}")
    private String secretKeyMaster;
    private SignatureAlgorithm SIGNATURE_ALG = SignatureAlgorithm.HS256;
    private static final String AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_TYPE = "Bearer";

    public String createToken(Long memberId, String type, String scope, Integer expire) {

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKeyMaster);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SIGNATURE_ALG.getJcaName());

        Map<String, Object> headerMap = new HashMap<>();

        headerMap.put("typ", "JWT");
        headerMap.put("alg", "HS256");

        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(memberId));
        map.put("type", type);
        map.put("scope", scope);

        JwtBuilder builder = Jwts.builder()
                .setHeader(headerMap)
                .setClaims(map)
                .setExpiration(DateUtils.addDays(new Date(), expire))
                .signWith(signingKey, SIGNATURE_ALG);

        return builder.compact();
    }

    public String getScope(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKeyMaster)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("scope",String.class);
    }

    public String getType(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKeyMaster)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("type", String.class);
    }

    public Long getMemberId(String token) {
        return Long.parseLong(Jwts.parserBuilder().setSigningKey(secretKeyMaster)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id", String.class));
    }

    public Map<String, String> getClaims(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKeyMaster)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Map<String, String> map = new HashMap<>();
        map.put("id", claims.get("id", String.class));
        map.put("scope", claims.get("scope", String.class));
        map.put("type", claims.get("type", String.class));
        return map;
    }

    public boolean validateToken(String token) {
        try {
            String key = secretKeyMaster;
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e){
            log.error("Token("+ token+ ") Expired");
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token("+ token+ ") Wrong");
            return false;
        }
    }

    public String getTokenByHeader(HttpServletRequest request) {
        String authorizationToken = request.getHeader(AUTHORIZATION);
        if (null != authorizationToken && authorizationToken.startsWith(AUTHORIZATION_TYPE)) {
            return authorizationToken.replace(AUTHORIZATION_TYPE, "").trim();
        } else {
            throw new CustomResponseStatusException(ExceptionCode.INTERNAL_ERROR,"");
        }
    }

    public Long getMemberIdByClaims(HttpServletRequest request) {
        String token = getTokenByHeader(request);
        if(token.equals(""))
            throw new CustomResponseStatusException(ExceptionCode.INTERNAL_ERROR,"");
        return getMemberId(token);
    }

    public String getScopeByClaims(HttpServletRequest request) {
        String token = getTokenByHeader(request);
        if(token.equals(""))
            throw new CustomResponseStatusException(ExceptionCode.INTERNAL_ERROR,"");
        return getScope(token);
    }
}
