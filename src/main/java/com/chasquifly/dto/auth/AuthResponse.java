package com.chasquifly.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de autenticación con tokens JWT")
public class AuthResponse {

    @Schema(description = "Token de acceso JWT válido por 15 minutos", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    /** Token de autorizacion Bearer con claims de usuario y expiracion corta (15 min). */
    private String accessToken;

    @Schema(description = "Token de refresco válido por 7 días", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;

    @Schema(description = "Tipo de token", example = "Bearer")
    @Builder.Default
    /** Esquema de autorizacion HTTP standard, por defecto Bearer. */
    private String tokenType = "Bearer";

    @Schema(description = "Tiempo de expiración del access token en segundos", example = "900")
    private long expiresIn;

    @Schema(description = "Datos básicos del usuario autenticado")
    private UserSummaryDto user;
}
