package com.sns.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtUtil {

    private static Claims extractClaims(String token, String key) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    public static boolean isExpired(String token, String key) {
//        log.info("토큰 유효기간 만료: {}", token);
//        Date expiredDate = extractClaims(token, key).getExpiration(); // expire timestamp를 return함
//        return expiredDate.before(new Date()); // 현재보다 이전인지 check를 합니다.

        return extractClaims(token, key)
                .getExpiration()
                .before(new Date());
    }

    public static String getUserName(String token, String key) {
        return extractClaims(token, key).get("userName").toString();
    }

    public static String createToken(String userName, String key, long expireTimeMs) {
        Claims claims = Jwts.claims();  // 일종의 map
        claims.put("userName", userName);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }
}
