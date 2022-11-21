package com.sik.template.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("spring.jwt.secret")
    private String secretKey;

    private long tokenValidMilisecond = 1000L * 60 * 60; // 1H

    private final UserDetailsService userDetailsService;


    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String uesrPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(uesrPk);
        claims.put("roles", roles);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMilisecond))      // 유효시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey)                      // 암호화 알고리즘 및 Key 설정
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Token 으로부터 회원 PK 추출
    public String getUserPk(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    // Request Header 로부터 token Parsing. "X-AUTH-TOKEN: TOKEN"
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    // Jwt Token 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jws<Claims> clames = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !clames.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
