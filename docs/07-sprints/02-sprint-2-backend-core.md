# Sprint 2: Desarrollo Backend Core y Persistencia

- **Duración:** Semanas 7 a 11.
- **Hito de Evaluación:** **Avance de Proyecto Final 2 (APF2)** (Semana 11).
- **Objetivo:** Construcción de la API REST en Spring Boot, integración con PostgreSQL (Single-Tenant) mediante Spring Data JPA y Flyway, autenticación JWT stateless y conexión de la interfaz con los datos reales.

---

## 👥 Asignación de Roles y Tareas (Sprint 2)

| Integrante / Rol | Foco Principal en Sprint 2 | Entregables Específicos |
| :--- | :--- | :--- |
| **Integrante 2 (Scrum Master S2)** | Gestión del Sprint 2 y coordinación del flujo Backend-DB | Configuración de arquitectura Spring Boot, perfiles de configuración `application.yml` |
| **Integrante 1 (Backend Dev)** | Persistencia y Migraciones Flyway | Scripts `V1` a `V3` de Flyway, entidades JPA (`Airport`, `Airline`, `Flight`) y repositorios |
| **Integrante 3 (Backend Dev)** | Seguridad y Autenticación JWT | `JwtService`, `SecurityConfig`, endpoints `/auth/register`, `/auth/login`, `/auth/refresh` |
| **Integrante 4 (Backend Dev)** | Módulo de Catálogos y Búsqueda de Vuelos | `FlightService`, filtros multicriterio con JPA Specifications / Queries optimizadas |
| **Integrante 5 (Backend Dev)** | Módulo de Estado de Vuelos en Vivo | `FlightStatusService`, endpoints para FIDS live board y cancelaciones |
| **Integrante 6 (Full Stack Dev)** | Integración Frontend -> Backend | Reemplazo de Mock Data por llamadas Axios reales a la API REST de Spring Boot |
| **Integrante 7 (QA / Testing)** | Pruebas Unitarias y de Integración | Pruebas JUnit 5 y Mockito para servicios de autenticación y búsqueda |

---

## 📋 Entregables Clave del Sprint 2 (APF2)
1. Backend Spring Boot 100% operativo conectado a PostgreSQL.
2. Base de datos versionada con Flyway con datos iniciales (Seeds) de aerolíneas, aeropuertos y vuelos.
3. Documentación interactiva de la API con OpenAPI / Swagger UI (`/swagger-ui.html`).
4. Autenticación funcional: registro, login y protección de rutas con JWT.
5. Búsqueda y visualización de vuelos conectada a la base de datos real.
