package com.chasquifly;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Prueba de integración base que certifica la carga completa del contexto de Spring Boot 3 (RNF-06).
 */
@SpringBootTest
@ActiveProfiles("test")
class ChasquiFlyApplicationTests {

    /** Certifica el arranque de beans, configuración de seguridad, repositorios JPA y servicios. */
    @Test
    void contextLoads() {
        // Basic context loading test
    }
}
