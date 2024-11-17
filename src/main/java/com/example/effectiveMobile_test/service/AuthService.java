package com.example.effectiveMobile_test.service;

import com.example.effectiveMobile_test.dto.AuthenticationResponse;
import com.example.effectiveMobile_test.exception.InvalidTokenException;
import com.example.effectiveMobile_test.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationResponse login(String username) {
        String accessToken = jwtTokenProvider.createAccessToken(username);
        String refreshToken = jwtTokenProvider.createRefreshToken(username);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public String getAccessTokenByRefreshToken(String refreshToken) {
        if (jwtTokenProvider.validateToken(refreshToken)) {
            String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
            return jwtTokenProvider.createAccessToken(username);
        } else {
            throw new InvalidTokenException("Invalid refresh token");
        }
    }
}
