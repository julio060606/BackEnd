package com.chasquifly.controller;

import com.chasquifly.dto.auth.AuthResponse;
import com.chasquifly.dto.auth.LoginRequest;
import com.chasquifly.dto.auth.RefreshTokenRequest;
import com.chasquifly.dto.auth.RegisterRequest;
import com.chasquifly.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "1. Autenticación", description = "Endpoints de registro, inicio de sesión y refresco de tokens JWT")
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint para el registro de nuevos usuarios en la plataforma.
     * Retorna HTTP 201 en caso de exito y HTTP 400 si los datos son invalidos o el email ya existe.
     */
    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario", description = "Crea una nueva cuenta de usuario y retorna el token JWT")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos de registro inválidos o email ya registrado",
            content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema()))
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint para autenticacion e inicio de sesion.
     * Retorna HTTP 200 con tokens JWT si las credenciales son validas, o HTTP 401 si son incorrectas.
     */
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica con email y contraseña, retornando tokens JWT")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Autenticación exitosa — retorna accessToken y refreshToken",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Credenciales incorrectas (email o contraseña inválida)",
            content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "400", description = "Cuerpo de la petición inválido",
            content = @Content(schema = @Schema()))
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refrescar token JWT", description = "Genera un nuevo access token utilizando un refresh token válido")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Token renovado exitosamente",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Refresh token expirado o inválido",
            content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "400", description = "Cuerpo de la petición inválido",
            content = @Content(schema = @Schema()))
    })
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }
}
