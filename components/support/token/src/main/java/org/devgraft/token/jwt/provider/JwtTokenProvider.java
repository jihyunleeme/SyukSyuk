package org.devgraft.token.jwt.provider;

import org.devgraft.token.jwt.config.properties.JwtProperties;
import org.devgraft.token.jwt.domain.JwtToken;
import org.devgraft.token.jwt.domain.JwtTokenInformation;
import org.devgraft.token.provider.DateProvider;
import org.devgraft.token.provider.TokenProvider;
import org.devgraft.token.provider.UuidProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider implements TokenProvider<JwtToken, JwtTokenGenerateRequest> {
    private final JwtProperties jwtProperties;
    private final UuidProvider uuidProvider;
    private final DateProvider dateProvider;

    @Override
    public JwtToken generate(JwtTokenGenerateRequest request) {
        Date date = dateProvider.now();
        String accessToken = _generate(date, request.getAccessTokenValidity());
        String refreshToken = _generate(date, request.getRefreshTokenValidity());

        return new JwtToken(new JwtTokenInformation(accessToken, refreshToken,
                request.getAccessTokenValidity(), request.getRefreshTokenValidity(), date));
    }

    private String _generate(Date currentDate, long validity) {
        Claims claims1 = Jwts.claims().setSubject(uuidProvider.randomUUID().toString());
        return Jwts.builder()
                .setClaims(claims1)
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + validity))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public boolean isExpiration(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException expiredJwtException) {
            expiredJwtException.printStackTrace();
            return true;
        }
        return false;
    }
}
