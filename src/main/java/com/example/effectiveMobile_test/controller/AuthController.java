package com.example.effectiveMobile_test.controller;

import com.example.effectiveMobile_test.dto.AuthenticationRequest;
import com.example.effectiveMobile_test.dto.AuthenticationResponse;
import com.example.effectiveMobile_test.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authService.login(request.getLogin());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestBody String refreshToken) {
        String newAccessToken = authService.getAccessTokenByRefreshToken(refreshToken);
        return ResponseEntity.ok(newAccessToken);
    }
}
