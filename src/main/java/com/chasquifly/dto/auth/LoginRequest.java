package com.chasquifly.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la solicitud de autenticacion de usuarios.
 * Contiene las credenciales requeridas (email con formato valido y contrasena no vacia).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud de inicio de sesión")
public class LoginRequest {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe proporcionar un formato de email válido")
    @Schema(description = "Correo electrónico del usuario", example = "usuario@chasquifly.com")
    /** Email registrado del usuario, verificado con formato estandar RFC. */
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description = "Contraseña de la cuenta", example = "Password123!")
    /** Contrasena en texto plano para verificacion contra el hash BCrypt. */
    private String password;
}
