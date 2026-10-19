package com.chasquifly.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
/**
 * Configuracion de OpenAPI 3 / Swagger UI para documentacion interactiva de la API.
 * Incluye la definicion del esquema de seguridad Bearer JWT para pruebas de endpoints protegidos.
 * 
 * @author SamuelHuamanHuillca
 */
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI chasquiFlyOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ChasquiFly - Flight Price Indexer API")
                        .description("API REST para indexación, comparación de precios de vuelos, seguimiento y alertas en tiempo real. Avance de Proyecto Final 2 (APF2).")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo ChasquiFly")
                                .email("contacto@chasquifly.com"))
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Entorno Local de Desarrollo"),
                        new Server().url("https://chasquifly-backend.onrender.com").description("Servidor Cloud (Render.com)")
                ))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingrese el token JWT obtenido en /api/v1/auth/login")));
    }
}
