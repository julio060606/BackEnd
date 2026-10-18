package com.chasquifly.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;
    private static final String TEST_SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final long TEST_EXPIRATION_MS = 3600000; // 1 hour
    private static final long TEST_REFRESH_EXPIRATION_MS = 86400000; // 24 hours

    private Authentication authentication;

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(tokenProvider, "jwtSecret", TEST_SECRET);
        ReflectionTestUtils.setField(tokenProvider, "jwtExpirationMs", TEST_EXPIRATION_MS);
        ReflectionTestUtils.setField(tokenProvider, "jwtRefreshExpirationMs", TEST_REFRESH_EXPIRATION_MS);

        UserPrincipal principal = new UserPrincipal(
                UUID.randomUUID(),
                "test@chasquifly.com",
                "Password123",
                "Juan",
                "Perez",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                true
        );

        authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    }

    @Test
    @DisplayName("Debe generar un Access Token JWT válido y no nulo")
    void testGenerateAccessToken() {
        String token = tokenProvider.generateAccessToken(authentication);

        assertNotNull(token);
        assertFalse(token.isBlank());
        assertTrue(tokenProvider.validateToken(token));
    }

    @Test
    @DisplayName("Debe extraer correctamente el email/subject del token JWT")
    void testGetUsernameFromJwt() {
        String token = tokenProvider.generateAccessToken(authentication);
        String username = tokenProvider.getUsernameFromJwt(token);

        assertEquals("test@chasquifly.com", username);
    }

    @Test
    @DisplayName("Debe validar que un token alterado o con firma incorrecta sea rechazado")
    void testValidateToken_InvalidSignature() {
        String token = tokenProvider.generateAccessToken(authentication);
        String tamperedToken = token + "corrupted";

        assertFalse(tokenProvider.validateToken(tamperedToken));
    }

    @Test
    @DisplayName("Debe generar un Refresh Token válido con claim subject correcto")
    void testGenerateRefreshToken() {
        String refreshToken = tokenProvider.generateRefreshToken("test@chasquifly.com");

        assertNotNull(refreshToken);
        assertTrue(tokenProvider.validateToken(refreshToken));
        assertEquals("test@chasquifly.com", tokenProvider.getUsernameFromJwt(refreshToken));
    }

    @Test
    @DisplayName("Debe rechazar un token expirado")
    void testValidateToken_ExpiredToken() {
        JwtTokenProvider expiredProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(expiredProvider, "jwtSecret", TEST_SECRET);
        ReflectionTestUtils.setField(expiredProvider, "jwtExpirationMs", -1000L); // Negative -> already expired
        ReflectionTestUtils.setField(expiredProvider, "jwtRefreshExpirationMs", TEST_REFRESH_EXPIRATION_MS);

        String expiredToken = expiredProvider.generateAccessToken(authentication);

        assertFalse(expiredProvider.validateToken(expiredToken));
    }
}
