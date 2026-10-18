package com.chasquifly.service;

import com.chasquifly.dto.auth.*;
import com.chasquifly.exception.BadRequestException;
import com.chasquifly.exception.ResourceNotFoundException;
import com.chasquifly.model.User;
import com.chasquifly.repository.UserRepository;
import com.chasquifly.security.JwtTokenProvider;
import com.chasquifly.security.UserPrincipal;
import com.chasquifly.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    private User sampleUser;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.randomUUID();
        sampleUser = User.builder()
                .id(userId)
                .email("demo@chasquifly.com")
                .passwordHash("hashed_password_123")
                .firstName("Demo")
                .lastName("Chasqui")
                .phone("+51999888777")
                .role("ROLE_USER")
                .isActive(true)
                .createdAt(OffsetDateTime.now())
                .build();

        UserPrincipal principal = new UserPrincipal(
                userId,
                "demo@chasquifly.com",
                "Password123",
                "Demo",
                "Chasqui",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                true
        );

        authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    }

    @Test
    @DisplayName("Debe registrar un nuevo usuario con éxito y retornar tokens JWT")
    void testRegister_Success() {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("Nuevo")
                .lastName("Usuario")
                .email("nuevo@chasquifly.com")
                .password("Password123")
                .phone("+51987654321")
                .build();

        when(userRepository.existsByEmail("nuevo@chasquifly.com")).thenReturn(false);
        when(passwordEncoder.encode("Password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(tokenProvider.generateAccessToken(any())).thenReturn("mocked-access-token");
        when(tokenProvider.generateRefreshToken(anyString())).thenReturn("mocked-refresh-token");
        when(tokenProvider.getExpirationMs()).thenReturn(86400000L);

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("mocked-access-token", response.getAccessToken());
        assertEquals("mocked-refresh-token", response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals("demo@chasquifly.com", response.getUser().getEmail());

        verify(userRepository, times(1)).existsByEmail("nuevo@chasquifly.com");
        verify(passwordEncoder, times(1)).encode("Password123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Debe lanzar BadRequestException al registrar con un email ya existente")
    void testRegister_DuplicateEmail() {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("Carlos")
                .lastName("Gomez")
                .email("demo@chasquifly.com")
                .password("Password123")
                .build();

        when(userRepository.existsByEmail("demo@chasquifly.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Debe autenticar con credenciales correctas y retornar AuthResponse")
    void testLogin_Success() {
        LoginRequest request = LoginRequest.builder()
                .email("demo@chasquifly.com")
                .password("Password123")
                .build();

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userRepository.findByEmail("demo@chasquifly.com")).thenReturn(Optional.of(sampleUser));
        when(tokenProvider.generateAccessToken(any())).thenReturn("mocked-access-token");
        when(tokenProvider.generateRefreshToken("demo@chasquifly.com")).thenReturn("mocked-refresh-token");
        when(tokenProvider.getExpirationMs()).thenReturn(86400000L);

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mocked-access-token", response.getAccessToken());
        assertEquals("demo@chasquifly.com", response.getUser().getEmail());
        assertEquals("ROLE_USER", response.getUser().getRole());
    }

    @Test
    @DisplayName("Debe propagar BadCredentialsException si la contraseña es incorrecta")
    void testLogin_InvalidCredentials() {
        LoginRequest request = LoginRequest.builder()
                .email("demo@chasquifly.com")
                .password("WrongPassword")
                .build();

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.login(request));
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si el usuario no existe tras la autenticación")
    void testLogin_UserNotFound() {
        LoginRequest request = LoginRequest.builder()
                .email("inexistente@chasquifly.com")
                .password("Password123")
                .build();

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userRepository.findByEmail("inexistente@chasquifly.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.login(request));
    }

    @Test
    @DisplayName("Debe refrescar el access token exitosamente con un refresh token válido")
    void testRefreshToken_Success() {
        RefreshTokenRequest request = new RefreshTokenRequest("valid-refresh-token");

        when(tokenProvider.validateToken("valid-refresh-token")).thenReturn(true);
        when(tokenProvider.getUsernameFromJwt("valid-refresh-token")).thenReturn("demo@chasquifly.com");
        when(userRepository.findByEmail("demo@chasquifly.com")).thenReturn(Optional.of(sampleUser));
        when(tokenProvider.generateAccessToken(any())).thenReturn("new-access-token");
        when(tokenProvider.generateRefreshToken("demo@chasquifly.com")).thenReturn("new-refresh-token");
        when(tokenProvider.getExpirationMs()).thenReturn(86400000L);

        AuthResponse response = authService.refreshToken(request);

        assertNotNull(response);
        assertEquals("new-access-token", response.getAccessToken());
        assertEquals("new-refresh-token", response.getRefreshToken());
    }

    @Test
    @DisplayName("Debe lanzar BadRequestException si el refresh token es inválido o expirado")
    void testRefreshToken_InvalidToken() {
        RefreshTokenRequest request = new RefreshTokenRequest("invalid-token");

        when(tokenProvider.validateToken("invalid-token")).thenReturn(false);

        assertThrows(BadRequestException.class, () -> authService.refreshToken(request));
        verify(userRepository, never()).findByEmail(anyString());
    }
}
