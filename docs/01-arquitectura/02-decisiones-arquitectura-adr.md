# Registro de Decisiones de Arquitectura (ADR)

Este registro documenta las decisiones técnicas y de infraestructura adoptadas para todo el ciclo de vida del proyecto.

---

## ADR 01: Enfoque Estricto Single-Tenant
- **Estado:** Aprobado / Obligatorio.
- **Contexto:** Se analizó si el sistema requería soporte multi-inquilino (Multi-Tenancy).
- **Decisión:** Descartar por completo el enfoque multi-tenant. La plataforma operará bajo un modelo de base de datos **Single-Tenant** normalizado en PostgreSQL con esquemas e índices optimizados por dominio.
- **Consecuencia:** Elimina la sobreingeniería de aislamiento por tenant (`tenant_id`, esquemas dinámicos), simplifica las migraciones con Flyway y optimiza el trabajo para un equipo de 7 desarrolladores.

---

## ADR 02: Pausa de Infraestructura Docker en Desarrollo Local
- **Estado:** Aprobado / En Modo Standby.
- **Contexto:** Las diferencias de hardware y sistemas operativos entre los 7 integrantes pueden ocasionar problemas al levantar múltiples contenedores en el flujo diario.
- **Decisión:** Mantener el código agnóstico. Se crean archivos `Dockerfile` y `docker-compose.yml` mínimos/comentados en modo placeholder. La ejecución diaria será directa (`npm run dev` en frontend y Maven/Java 17+ local en backend).
- **Consecuencia:** Mayor velocidad de desarrollo y depuración inmediata en IDEs sin depender de Docker Desktop.

---

## ADR 03: Integración de Caché Redis Mediante Feature Flag
- **Estado:** Aprobado / Condicional.
- **Contexto:** Implementar caché para resultados de búsqueda de vuelos es óptimo para rendimiento, pero exigir un servidor Redis local activo dificulta el entorno local.
- **Decisión:** La infraestructura de Redis en Spring Boot se configura condicionalmente con `@ConditionalOnProperty(name = "app.cache.enabled", havingValue = "true")`. Por defecto (`false`), el sistema interactúa directamente con PostgreSQL.
- **Consecuencia:** Se mantiene la capacidad de activar caché para pruebas de carga o sustentación final sin romper el entorno de desarrollo estándar.

---

## ADR 04: Estrategia de Validación Simétrica (Zod + Bean Validation)
- **Estado:** Aprobado / Obligatorio.
- **Contexto:** Evitar discrepancias entre las restricciones de la interfaz y las de la base de datos/backend.
- **Decisión:** Se utiliza **Zod** en React para validación en tiempo real y feedback visual, y **Jakarta Bean Validation** (`@Valid`, `@NotBlank`, etc.) en Spring Boot como barrera de seguridad inviolable.
- **Consecuencia:** Experiencia de usuario ágil y seguridad multicapa garantizada.
