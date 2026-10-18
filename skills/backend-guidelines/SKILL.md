---
name: backend-guidelines
description: Guía de estándares de codificación, arquitectura en capas, seguridad y validaciones para Spring Boot en ChasquiFly.
---

# Skill: ChasquiFly Backend Development Guidelines

Esta skill establece las normas arquitectónicas y de desarrollo para el backend en Spring Boot 3 con Java 21 del proyecto ChasquiFly.

---

## 🏛️ 1. Estructura de Paquetes
Todo el código debe ubicarse bajo el paquete base `com.chasquifly`:
- `config`: Clases de configuración Spring (`@Configuration`), CORS, OpenAPI y clientes externos.
- `security`: Configuración de Spring Security, filtros JWT, proveedor de tokens y `UserDetails`.
- `model`: Entidades JPA anotadas con `@Entity`, `@Table`, `@Id`, relaciones `@ManyToOne` y timestamps.
- `repository`: Interfaces que extienden `JpaRepository` o `JpaSpecificationExecutor`.
- `dto`: Clases de transferencia de datos organizadas por dominio (`auth`, `flight`, `airport`, `trip`, `common`).
- `service`: Interfaces de lógica de negocio pura.
- `service.impl`: Implementaciones concretas anotadas con `@Service` y `@Transactional`.
- `controller`: Endpoints REST anotados con `@RestController`, `@RequestMapping` y etiquetas OpenAPI (`@Tag`, `@Operation`).
- `client`: Clientes HTTP externos (ej. `client.duffel`).
- `exception`: Manejador global `@RestControllerAdvice` y excepciones personalizadas.

---

## 🛡️ 2. Reglas de Seguridad y Validación
1. **Validación de Entradas:**
   - Todos los DTOs que reciben peticiones en `@RequestBody` deben estar anotados con `@Valid`.
   - Utilizar validaciones de `jakarta.validation.constraints` (`@NotBlank`, `@NotNull`, `@Email`, `@Size`, `@Min`).
2. **Autenticación Stateless:**
   - No utilizar sesiones de servidor (`SessionCreationPolicy.STATELESS`).
   - El token JWT se transmite siempre en la cabecera `Authorization: Bearer <token>`.
3. **CORS:**
   - Mantener centralizada la configuración en `CorsConfig` y `application.yml`.
   - Permitir orígenes de Vite (`http://localhost:5173`) y producción Vercel.

---

## 💾 3. Persistencia y Transaccionalidad
1. **Transacciones:**
   - Anotar los métodos de solo lectura en servicios con `@Transactional(readOnly = true)`.
   - Métodos de escritura (crear, actualizar, eliminar) deben ser `@Transactional`.
2. **Entidades JPA:**
   - Las llaves primarias usan `UUID` generado automáticamente para entidades transaccionales y `Integer` (`IDENTITY`) para catálogos.
   - En relaciones `@ManyToOne`, especificar `FetchType.LAZY` para rendimiento, salvo catálogos ligeros como `Airport` y `Airline` que pueden ser `FetchType.EAGER`.
