package com.chasquifly.service;

import com.chasquifly.dto.auth.AuthResponse;
import com.chasquifly.dto.auth.LoginRequest;
import com.chasquifly.dto.auth.RefreshTokenRequest;
import com.chasquifly.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);
}
