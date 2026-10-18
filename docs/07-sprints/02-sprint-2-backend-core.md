# Sprint 2: Desarrollo Backend Core, Persistencia y Despliegue en la Nube (APF2)

- **Hito Académico:** **Avance de Proyecto Final 2 (APF2)** (Semana 11).
- **Asignatura:** Herramientas de Desarrollo.
- **Objetivo General:** Construcción de la API REST en Spring Boot 3 (Java 21), persistencia relacional con PostgreSQL en la nube (Supabase) versionada obligatoriamente con Flyway, seguridad Stateless con Bearer JWT, integración con el cliente externo de Duffel API, documentación interactiva OpenAPI 3 (Swagger UI) y despliegue continuo en la nube (**Render.com**).

---

## 🏛️ 1. Alineación con el Sílabo y Rúbrica de Evaluación (Profesor)

Cada criterio de la rúbrica del docente (valorado en 5 puntos) se encuentra cubierto por las responsabilidades del equipo:

| Criterio de la Rúbrica | Pts | Requisito del Sílabo / Profesor | Implementación en ChasquiFly | Rama Responsable |
| :--- | :---: | :--- | :--- | :--- |
| **1. Infraestructura Backend** | 5 | Proyecto Spring Boot con Maven funcional, empaquetado `.jar` y conexión a base de datos. | Spring Boot 3.3.4, Java 21, conexión HikariCP a PostgreSQL en Supabase vía SSL. | `feature/backend-base-security` |
| **2. Modelo de Datos y Migraciones** | 5 | Esquema relacional normalizado (3FN), relaciones JPA y **Flyway obligatorio** (`V1`, `V2`). | Entidades con `@ManyToOne`, scripts `V1__create_tables.sql` y `V2__seed_initial_data.sql`. | `feature/database-flyway-airports` |
| **3. Endpoints Principales (CRUDs)** | 5 | Endpoints funcionales para entidades base con validaciones (`@Valid`). | CRUD completo de vuelos, catálogos de aeropuertos, búsqueda multicriterio y validaciones Bean Validation. | `feature/flight-search-crud` y `feature/flight-search-engine` |
| **4. Autenticación y Seguridad** | 5 | Autenticación basada en **JWT o sesiones**, encriptación segura y middleware de protección. | Spring Security 6 con `JwtAuthenticationFilter`, contraseñas con `BCrypt` y roles `ROLE_USER` / `ROLE_ADMIN`. | `feature/auth-user-module` |
| **5. Integración Frontend** | 5 | El Frontend React debe consumir los endpoints reales del backend. | Consumo de `/api/v1/auth`, `/api/v1/flights` y `/api/v1/airports` alternando `VITE_USE_MOCKS=false`. | Asignado por módulo (Ver Sección 5) |
| **6. Gestión Git & Jira** | 5 | Flujo GitFlow (`dev`, `feature/*`), commits atómicos y tablero Jira al día. | Ramas por historia de usuario, commits semánticos y 8 subtareas por integrante en Jira. | Todas las ramas |
| **7. Despliegue en Render.com** | 5 | **Despliegue operativo en Render.com** con variables de entorno protegidas. | `Dockerfile` multi-stage, `render.yaml` y Web Service activo con healthcheck `/actuator/health`. | `feature/duffel-devops-render` |
| **8-11. Calidad PPT y Sustentación** | 20 | Sustentación técnica, diagramas claros y dominio del código sin código puro en PPT. | Diapositivas 50% capturas de arquitectura / 50% ideas clave, explicación de commits y Jira. | Todo el equipo |

---

## 🤝 2. Incorporación de los Aportes del Equipo

1. **Aporte de Kevin (Arquitectura y APIs Externas):**
   - Kevin propuso estructurar el backend de forma modular en paquetes (`config`, `controller`, `service`, `client`, `dto`, `model`, `repository`) e investigó APIs de vuelos (Amadeus y Duffel).
   - *Decisión técnica:* Se adoptó su arquitectura modular y se integró el cliente de **Duffel API** en `client/duffel/` con un patrón de **Fallback Gratuito**: si el sandbox de Duffel expira o no hay credenciales, el sistema consulta de forma transparente la base de datos interna de PostgreSQL, asegurando que la aplicación **nunca falle** ante el profesor.
2. **Aporte del Compañero de Base de Datos (Supabase):**
   - El compañero aprovisionó la instancia de PostgreSQL en la nube de Supabase y facilitó las credenciales.
   - *Decisión técnica:* La base de datos es 100% en la nube (Single-Tenant, 3FN). La contraseña (`N8JWdXvzO6vfFt7x`) se gestiona mediante variables de entorno (`SPRING_DATASOURCE_PASSWORD`), evitando exponerla en commits de Git y garantizando persistencia permanente (a diferencia del PostgreSQL de Render que se borra a los 30 días).

---

## 👥 3. Matriz de Asignación por Integrante (Ramas, Archivos Exclusivos y Tareas)

Para garantizar **cero conflictos de merge (0 Merge Conflicts)** en GitFlow, cada integrante tiene asignado un conjunto de archivos **exclusivos** que solo su rama debe modificar.

---

### 👤 Integrante 1 (Julio - Tech Lead & Scrum Master)
- **Rama:** `feature/backend-base-security`
- **Historia de Usuario (Jira):** `US-S2-01: Configuración de la Arquitectura Base, Seguridad JWT, CORS y Manejo Global de Errores`
- **Archivos Exclusivos a Modificar / Crear:**
  - `BackEnd/pom.xml`
  - `BackEnd/src/main/resources/application.yml`
  - `BackEnd/src/main/resources/application-dev.yml`
  - `BackEnd/src/main/resources/application-prod.yml`
  - `BackEnd/src/main/java/com/chasquifly/ChasquiFlyApplication.java`
  - `BackEnd/src/main/java/com/chasquifly/config/CorsConfig.java`
  - `BackEnd/src/main/java/com/chasquifly/config/OpenApiConfig.java`
  - `BackEnd/src/main/java/com/chasquifly/security/SecurityConfig.java`
  - `BackEnd/src/main/java/com/chasquifly/security/JwtTokenProvider.java`
  - `BackEnd/src/main/java/com/chasquifly/security/JwtAuthenticationFilter.java`
  - `BackEnd/src/main/java/com/chasquifly/security/JwtAuthenticationEntryPoint.java`
  - `BackEnd/src/main/java/com/chasquifly/exception/GlobalExceptionHandler.java`
  - `BackEnd/src/main/java/com/chasquifly/exception/ErrorResponse.java`
  - `BackEnd/src/main/java/com/chasquifly/dto/common/ApiResponse.java`
- **Subtareas en Jira (8 subtareas):**
  1. `S2-T1.1`: Scaffolding del proyecto Maven con Spring Boot 3.3.4 y Java 21 en `BackEnd/pom.xml`.
  2. `S2-T1.2`: Configuración de perfiles `application.yml`, `application-dev.yml` y `application-prod.yml`.
  3. `S2-T1.3`: Implementación de `CorsConfig` con soporte para Vite local y Vercel en producción.
  4. `S2-T1.4`: Implementación de `JwtTokenProvider` con firma HMAC-SHA256 y expiración configurable.
  5. `S2-T1.5`: Implementación del filtro `JwtAuthenticationFilter` y punto de entrada `JwtAuthenticationEntryPoint`.
  6. `S2-T1.6`: Definición de `SecurityConfig` (rutas públicas `/api/v1/auth/**`, `/swagger-ui/**` vs protegidas).
  7. `S2-T1.7`: Creación del manejador global de excepciones `GlobalExceptionHandler` con DTOs de error estandarizados.
  8. `S2-T1.8`: Configuración de OpenAPI 3 (`OpenApiConfig`) con soporte para esquemas Bearer JWT.
- **El "Cómo Implementarlo" (Paso a Paso):**
  1. Configurar Spring Security 6 con `SessionCreationPolicy.STATELESS` en `SecurityConfig.java`.
  2. Implementar `JwtTokenProvider.java` usando `Keys.hmacShaKeyFor(secret.getBytes())` para firmar tokens con claims (`userId`, `role`, `firstName`).
  3. Crear `JwtAuthenticationFilter.java` interceptando el header `Authorization: Bearer <token>` y cargando el contexto en `SecurityContextHolder`.
  4. En `CorsConfig.java`, habilitar `http://localhost:5173` y los dominios de Vercel/Render para evitar bloqueos del navegador.
  5. En `GlobalExceptionHandler.java`, usar `@RestControllerAdvice` con `@ExceptionHandler` para retornar JSONs estructurados ante `MethodArgumentNotValidException` y `ResourceNotFoundException`.

---

### 👤 Integrante 2 (Backend Dev)
- **Rama:** `feature/auth-user-module`
- **Historia de Usuario (Jira):** `US-S2-02: Módulo de Autenticación, Registro de Usuarios y Gestión de Sesiones JWT`
- **Archivos Exclusivos a Modificar / Crear:**
  - `BackEnd/src/main/java/com/chasquifly/model/User.java`
  - `BackEnd/src/main/java/com/chasquifly/repository/UserRepository.java`
  - `BackEnd/src/main/java/com/chasquifly/security/UserPrincipal.java`
  - `BackEnd/src/main/java/com/chasquifly/security/UserDetailsServiceImpl.java`
  - `BackEnd/src/main/java/com/chasquifly/dto/auth/LoginRequest.java`
  - `BackEnd/src/main/java/com/chasquifly/dto/auth/RegisterRequest.java`
  - `BackEnd/src/main/java/com/chasquifly/dto/auth/AuthResponse.java`
  - `BackEnd/src/main/java/com/chasquifly/dto/auth/UserSummaryDto.java`
  - `BackEnd/src/main/java/com/chasquifly/dto/auth/RefreshTokenRequest.java`
  - `BackEnd/src/main/java/com/chasquifly/service/AuthService.java`
  - `BackEnd/src/main/java/com/chasquifly/service/impl/AuthServiceImpl.java`
  - `BackEnd/src/main/java/com/chasquifly/controller/AuthController.java`
  - `FrontEnd/src/services/authService.ts` (solo para desactivar mocks)
- **Subtareas en Jira (8 subtareas):**
  1. `S2-T2.1`: Creación de la entidad JPA `User` mapeada a la tabla `users`.
  2. `S2-T2.2`: Creación del repositorio `UserRepository` con búsqueda por email y validación de existencia.
  3. `S2-T2.3`: Implementación de `UserPrincipal` y `UserDetailsServiceImpl` para Spring Security.
  4. `S2-T2.4`: Creación de DTOs de autenticación (`LoginRequest`, `RegisterRequest`, `AuthResponse`, `UserSummaryDto`).
  5. `S2-T2.5`: Implementación de `AuthService` con validación de duplicados y encriptación BCrypt.
  6. `S2-T2.6`: Implementación del endpoint `POST /api/v1/auth/register` con código `201 Created`.
  7. `S2-T2.7`: Implementación del endpoint `POST /api/v1/auth/login` con validación de credenciales.
  8. `S2-T2.8`: Implementación del endpoint `POST /api/v1/auth/refresh` para renovación de access tokens.
- **El "Cómo Implementarlo" (Paso a Paso):**
  1. Mapear en `User.java` los campos `email` (único), `passwordHash`, `firstName`, `lastName` y `role` (`ROLE_USER`).
  2. En `UserRepository.java`, definir `findByEmail(String email)` y `existsByEmail(String email)`.
  3. En `AuthServiceImpl.java`, inyectar `PasswordEncoder` (`BCrypt`) para hashear la contraseña en el registro.
  4. Para el login, utilizar `authenticationManager.authenticate(...)` y generar tanto `accessToken` (24h) como `refreshToken` (7 días).
  5. En `AuthController.java`, exponer `POST /api/v1/auth/register` (201 Created), `POST /api/v1/auth/login` (200 OK) y `POST /api/v1/auth/refresh`, validando que el body devuelto coincida exactamente con lo que espera `authService.ts` en React.
  6. Desactivar mocks en el Frontend (`USE_MOCKS = false` en `authService.ts`) y verificar que se inicie sesión con `demo@chasquifly.com` / `Password123`.

---

### 👤 Integrante 3 (Backend Dev - Aporte Supabase)
- **Rama:** `feature/database-flyway-airports`
- **Historia de Usuario (Jira):** `US-S2-03: Persistencia en Supabase, Migraciones Flyway y Catálogo de Aeropuertos y Aerolíneas`
- **Archivos Exclusivos a Modificar / Crear:**
  - `BackEnd/src/main/resources/db/migration/V1__create_tables.sql`
  - `BackEnd/src/main/resources/db/migration/V2__seed_initial_data.sql`
  - `BackEnd/src/main/java/com/chasquifly/model/Airport.java`
  - `BackEnd/src/main/java/com/chasquifly/model/Airline.java`
  - `BackEnd/src/main/java/com/chasquifly/repository/AirportRepository.java`
  - `BackEnd/src/main/java/com/chasquifly/repository/AirlineRepository.java`
  - `BackEnd/src/main/java/com/chasquifly/dto/airport/AirportDto.java`
  - `BackEnd/src/main/java/com/chasquifly/dto/airport/AirlineDto.java`
  - `BackEnd/src/main/java/com/chasquifly/service/AirportService.java`
  - `BackEnd/src/main/java/com/chasquifly/service/impl/AirportServiceImpl.java`
  - `BackEnd/src/main/java/com/chasquifly/controller/AirportController.java`
  - `FrontEnd/src/services/homeService.ts` (solo función `getAirports`)
- **Subtareas en Jira (8 subtareas):**
  1. `S2-T3.1`: Configuración de la conexión JDBC hacia la instancia PostgreSQL de Supabase vía SSL.
  2. `S2-T3.2`: Redacción del script de migración inicial `V1__create_tables.sql` con constraints e índices.
  3. `S2-T3.3`: Redacción del script de semillado `V2__seed_initial_data.sql` con aeropuertos peruanos y aerolíneas.
  4. `S2-T3.4`: Creación de entidades JPA `Airport` y `Airline` con llaves primarias autoincrementales.
  5. `S2-T3.5`: Implementación de `AirportRepository` y `AirlineRepository` con consultas por código IATA.
  6. `S2-T3.6`: Creación de los DTOs `AirportDto` y `AirlineDto`.
  7. `S2-T3.7`: Implementación de `AirportService` y `AirportServiceImpl`.
  8. `S2-T3.8`: Implementación del controlador `AirportController` (`GET /api/v1/airports`, `GET /api/v1/airports/{iataCode}`).
- **El "Cómo Implementarlo" (Paso a Paso):**
  1. En `V1__create_tables.sql`, escribir el DDL con `CREATE TABLE IF NOT EXISTS` para `airports`, `airlines`, `flights`, `users`, `user_trips` y `notifications_queue`.
  2. En `V2__seed_initial_data.sql`, insertar aeropuertos nacionales (LIM, CUZ, AQP, TRU, IQT) y aerolíneas (LATAM, Sky, JetSMART).
  3. Configurar en `application.yml` el flag `spring.flyway.baseline-on-migrate: true` para que Flyway controle las versiones de la BD.
  4. Crear `AirportController.java` con `GET /api/v1/airports` para que el componente `FlightSearchForm.tsx` del Frontend cargue los orígenes y destinos de forma dinámica.
  5. Desactivar mocks en `homeService.ts` (`USE_MOCKS = false` en `getAirports()`) y comprobar que el selector de aeropuertos muestre los datos reales de Supabase.

---

### 👤 Integrante 4 (Backend Dev)
- **Rama:** `feature/flight-search-crud`
- **Historia de Usuario (Jira):** `US-S2-04: Entidad de Vuelos, Persistencia Relacional y Operaciones CRUD de Administración`
- **Archivos Exclusivos a Modificar / Crear:**
  - `BackEnd/src/main/java/com/chasquifly/model/Flight.java`
  - `BackEnd/src/main/java/com/chasquifly/dto/flight/CreateFlightDto.java`
  - `BackEnd/src/main/java/com/chasquifly/service/FlightService.java` (bloque de métodos CRUD)
  - `BackEnd/src/main/java/com/chasquifly/service/impl/FlightServiceImpl.java` (bloque CRUD: `createFlight`, `updateFlight`, `deleteFlight`, `getFlightById`)
  - `BackEnd/src/main/java/com/chasquifly/controller/FlightController.java` (bloque CRUD: `@PostMapping`, `@PutMapping`, `@DeleteMapping`, `@GetMapping("/{id}")`)
- **Subtareas en Jira (8 subtareas):**
  1. `S2-T4.1`: Creación de la entidad JPA `Flight` con relaciones `@ManyToOne` a `Airline` y `Airport`.
  2. `S2-T4.2`: Creación de `FlightRepository` con soporte para consultas derivadas y paginación.
  3. `S2-T4.3`: Creación de los DTOs `CreateFlightDto` y `FlightResponseDto` con validaciones `@DecimalMin`, `@NotNull`.
  4. `S2-T4.4`: Implementación del método `getFlightById(UUID)` con excepción `ResourceNotFoundException`.
  5. `S2-T4.5`: Implementación del método de creación `createFlight(CreateFlightDto)` con transaccionalidad.
  6. `S2-T4.6`: Implementación del método de actualización `updateFlight(UUID, CreateFlightDto)`.
  7. `S2-T4.7`: Implementación del método de eliminación `deleteFlight(UUID)`.
  8. `S2-T4.8`: Exposición de los endpoints REST en `FlightController` con roles de administración (`ROLE_ADMIN`).
- **El "Cómo Implementarlo" (Paso a Paso):**
  1. En `Flight.java`, mapear relaciones `@ManyToOne(fetch = FetchType.EAGER)` hacia `Airport` (origen y destino) y `Airline`.
  2. En `CreateFlightDto.java`, aplicar anotaciones de validación `@NotBlank`, `@NotNull`, `@DecimalMin(value = "0.01")` y `@Min(1)`.
  3. En `FlightServiceImpl.java`, implementar `createFlight` buscando primero que existan la aerolínea y los aeropuertos referenciados; si no existen, lanzar `ResourceNotFoundException`.
  4. En `FlightController.java`, asegurar los métodos de creación, modificación y borrado con autorización o anotación de administración (`ROLE_ADMIN`).
  5. Probar desde Swagger UI la creación de un nuevo vuelo y verificar en Supabase Table Editor que la fila aparezca inmediatamente.

---

### 👤 Integrante 5 (Backend Dev)
- **Rama:** `feature/flight-search-engine`
- **Historia de Usuario (Jira):** `US-S2-05: Motor de Búsqueda Dinámica Multicriterio, Filtros y Ofertas Populares`
- **Archivos Exclusivos a Modificar / Crear:**
  - `BackEnd/src/main/java/com/chasquifly/repository/FlightRepository.java` (consultas `@Query` de búsqueda)
  - `BackEnd/src/main/java/com/chasquifly/dto/flight/FlightSearchCriteria.java`
  - `BackEnd/src/main/java/com/chasquifly/dto/flight/FlightResponseDto.java`
  - `BackEnd/src/main/java/com/chasquifly/service/FlightService.java` (bloque búsqueda: `searchFlights`, `getPopularDestinations`, `getAiRecommendation`)
  - `BackEnd/src/main/java/com/chasquifly/service/impl/FlightServiceImpl.java` (bloque búsqueda: `searchFlights`, `getPopularDestinations`, `getAiRecommendation`)
  - `BackEnd/src/main/java/com/chasquifly/controller/FlightController.java` (métodos `GET /search`, `GET /popular-destinations`, `GET /ai-recommendation`)
  - `FrontEnd/src/services/flightService.ts` (solo función `searchFlights`)
- **Subtareas en Jira (8 subtareas):**
  1. `S2-T5.1`: Creación del DTO `FlightSearchCriteria` con parámetros de fecha, origen, destino, precio y escalas.
  2. `S2-T5.2`: Definición de consultas JPQL optimizadas en `FlightRepository` usando los índices de base de datos.
  3. `S2-T5.3`: Implementación de filtros multicriterio en `FlightServiceImpl` para aerolíneas y precios máximos.
  4. `S2-T5.4`: Implementación de lógica de ordenamiento (`PRICE_ASC`, `DURATION_ASC`, `BEST`).
  5. `S2-T5.5`: Implementación del endpoint `GET /api/v1/flights/search` compatible con el contrato de `flightService.ts`.
  6. `S2-T5.6`: Implementación de lógica de destinos populares en `getPopularDestinations()`.
  7. `S2-T5.7`: Implementación de recomendación de mejor opción costo-beneficio en `getAiRecommendation()`.
  8. `S2-T5.8`: Pruebas de integración de búsqueda con escenarios de vuelos directos y con escalas.
- **El "Cómo Implementarlo" (Paso a Paso):**
  1. En `FlightRepository.java`, crear una consulta JPQL con parámetros opcionales (`:originCode IS NULL OR ...`) aprovechando el índice compuesto `idx_flights_search`.
  2. En `FlightSearchCriteria.java`, recibir los parámetros de la URL: `origin`, `destination`, `departureDate`, `maxPrice`, `airlineId`, `maxStops`.
  3. En `FlightServiceImpl.java`, aplicar lógica de ordenamiento (`PRICE_ASC`, `DURATION_ASC`, `BEST` con balance de precio y duración).
  4. En `FlightController.java`, retornar la lista directa de `FlightResponseDto` con status 200, alineada con el contrato de `flightService.ts` en el Frontend.
  5. En `FrontEnd/src/services/flightService.ts`, cambiar `USE_MOCKS = false`, descomentar la llamada `axios.get('/api/v1/flights/search')` y probar en `/flights` que las tarjetas de vuelos rendericen los datos reales de Supabase.

---

### 👤 Integrante 6 (Backend Dev)
- **Rama:** `feature/flight-compare-tracking`
- **Historia de Usuario (Jira):** `US-S2-06: Comparador de Vuelos, Tablero FIDS en Vivo e Itinerarios de Viajes Guardados`
- **Archivos Exclusivos a Modificar / Crear:**
  - `BackEnd/src/main/java/com/chasquifly/model/UserTrip.java`
  - `BackEnd/src/main/java/com/chasquifly/model/NotificationQueue.java`
  - `BackEnd/src/main/java/com/chasquifly/repository/UserTripRepository.java`
  - `BackEnd/src/main/java/com/chasquifly/repository/NotificationQueueRepository.java`
  - `BackEnd/src/main/java/com/chasquifly/dto/flight/FlightComparisonDto.java`
  - `BackEnd/src/main/java/com/chasquifly/dto/trip/CreateTripDto.java`
  - `BackEnd/src/main/java/com/chasquifly/dto/trip/TripResponseDto.java`
  - `BackEnd/src/main/java/com/chasquifly/service/FlightStatusService.java` & `FlightStatusServiceImpl.java`
  - `BackEnd/src/main/java/com/chasquifly/service/TripService.java` & `TripServiceImpl.java`
  - `BackEnd/src/main/java/com/chasquifly/controller/FlightStatusController.java`
  - `BackEnd/src/main/java/com/chasquifly/controller/TripController.java`
  - `FrontEnd/src/services/compareService.ts` y `FrontEnd/src/services/trackerService.ts` (solo para desactivar mocks)
- **Subtareas en Jira (8 subtareas):**
  1. `S2-T6.1`: Creación de la entidad JPA `UserTrip` para vincular usuarios con vuelos guardados.
  2. `S2-T6.2`: Creación del repositorio `UserTripRepository` con consultas por usuario.
  3. `S2-T6.3`: Creación de DTOs `CreateTripDto`, `TripResponseDto` y `FlightComparisonDto`.
  4. `S2-T6.4`: Implementación del servicio de comparación `compareFlights(List<UUID>)` calculando diferencias de tarifa y duración.
  5. `S2-T6.5`: Exposición del endpoint `POST /api/v1/flights/compare` para la vista de comparación del frontend.
  6. `S2-T6.6`: Implementación de `FlightStatusServiceImpl` con consultas para salidas (DEPARTURES) y llegadas (ARRIVALS).
  7. `S2-T6.7`: Exposición del endpoint `GET /api/v1/flight-status/live` y seguimiento por número `GET /track/{flightNumber}`.
  8. `S2-T6.8`: Implementación de endpoints de itinerarios de usuario (`GET /api/v1/trips`, `POST /api/v1/trips`, `DELETE /api/v1/trips/{id}`).
- **El "Cómo Implementarlo" (Paso a Paso):**
  1. En `FlightStatusServiceImpl.java`, implementar `getLiveBoard(airport, type)` consultando vuelos con estado `BOARDING`, `DELAYED`, `SCHEDULED` para simular la pantalla FIDS de un aeropuerto real.
  2. En `compareFlights(List<UUID>)`, recibir la lista de vuelos seleccionados en el comparador, calcular el vuelo más económico, el más veloz y la diferencia de tarifas (`priceDifference`).
  3. En `TripServiceImpl.java`, guardar y listar los viajes vinculados al usuario autenticado mediante `@AuthenticationPrincipal UserPrincipal user`.
  4. Desactivar mocks en `compareService.ts` y `trackerService.ts` y comprobar que en `/tracker` al escribir `LA2040` muestre el estado en vivo real.

---

### 👤 Integrante 7 (Kevin - DevOps & External API Lead)
- **Rama:** `feature/duffel-devops-render`
- **Historia de Usuario (Jira):** `US-S2-07: Cliente de Integración Externa con Duffel API, Contenerización Docker y Despliegue en Render.com`
- **Archivos Exclusivos a Modificar / Crear:**
  - `BackEnd/src/main/java/com/chasquifly/config/DuffelConfig.java`
  - `BackEnd/src/main/java/com/chasquifly/client/duffel/DuffelClient.java`
  - `BackEnd/src/main/java/com/chasquifly/client/duffel/DuffelService.java`
  - `BackEnd/src/main/java/com/chasquifly/client/duffel/dto/DuffelSearchRequest.java`
  - `BackEnd/src/main/java/com/chasquifly/client/duffel/dto/DuffelOfferResponse.java`
  - `BackEnd/Dockerfile`
  - `BackEnd/render.yaml`
  - `BackEnd/README.md`
  - `FrontEnd/.env.production` (configurar URL del backend en Render)
- **Subtareas en Jira (8 subtareas):**
  1. `S2-T7.1`: Creación de la clase de configuración `DuffelConfig` mapeada a propiedades `app.duffel.*`.
  2. `S2-T7.2`: Creación de DTOs para solicitudes y respuestas de Duffel (`DuffelSearchRequest`, `DuffelOfferResponse`).
  3. `S2-T7.3`: Implementación de `DuffelClient` con manejo de fallback condicional cuando la API externa no esté disponible.
  4. `S2-T7.4`: Implementación del wrapper de servicio `DuffelService` para enriquecimiento de catálogos.
  5. `S2-T7.5`: Creación y optimización del `Dockerfile` multi-stage con imagen Eclipse Temurin Java 21 JRE.
  6. `S2-T7.6`: Creación del archivo de especificación de infraestructura como código `render.yaml`.
  7. `S2-T7.7`: Configuración del servicio Web Service en Render.com con healthcheck `/actuator/health`.
  8. `S2-T7.8`: Verificación de la conexión entre el frontend desplegado y el backend en Render.com.
- **El "Cómo Implementarlo" (Paso a Paso):**
  1. En `DuffelConfig.java`, mapear la propiedad `app.duffel.enabled: false` (por defecto desactivado para no depender de la cuota sandbox).
  2. En `DuffelClient.java`, envolver la llamada HTTP en un bloque `try-catch` con degradación elegante: si el token de Duffel no está configurado, retorna una lista vacía y el sistema opera con la base de datos interna sin emitir excepciones 500.
  3. En `Dockerfile`, utilizar una construcción multi-stage con `eclipse-temurin:21-jdk` para compilar con Maven y `eclipse-temurin:21-jre` para ejecutar con usuario no privilegiado (`chasquifly`).
  4. En Render.com, crear un **Web Service**, vincular el repositorio, seleccionar la carpeta `BackEnd`, definir el healthcheck en `/actuator/health` y cargar las variables de entorno de Supabase y JWT.
  5. Configurar `FrontEnd/.env.production` con la URL HTTPS de Render y probar que Vercel se comunique sin errores de CORS ni Mixed Content.

---

## 📖 4. Catálogo Guiado de Endpoints REST (Contratos Exactos)

Todos los endpoints tienen como prefijo base `/api/v1`.

### 4.1. Módulo de Autenticación (`/api/v1/auth`) — Integrante 2
* **`POST /api/v1/auth/register`** (Público)
  - **Body Enviado:**
    ```json
    { "firstName": "Carlos", "lastName": "Mamani", "email": "carlos@example.com", "password": "Password123", "phone": "+51987654321" }
    ```
  - **Respuesta Esperada (`201 Created`):**
    ```json
    { "success": true, "message": "Usuario registrado exitosamente", "data": { "accessToken": "eyJ...", "refreshToken": "eyJ...", "tokenType": "Bearer", "expiresIn": 86400, "user": { "id": "uuid", "email": "carlos@example.com", "firstName": "Carlos", "lastName": "Mamani", "role": "ROLE_USER" } } }
    ```
* **`POST /api/v1/auth/login`** (Público)
  - **Body Enviado:** `{ "email": "carlos@example.com", "password": "Password123" }`
  - **Respuesta Esperada (`200 OK`):** Mismo objeto que en registro.
* **`POST /api/v1/auth/refresh`** (Público)
  - **Body Enviado:** `{ "refreshToken": "eyJ..." }`
  - **Respuesta Esperada (`200 OK`):** Nuevo `accessToken` y `refreshToken`.

---

### 4.2. Módulo de Aeropuertos y Catálogos (`/api/v1/airports`) — Integrante 3
* **`GET /api/v1/airports`** (Público)
  - **Respuesta Esperada (`200 OK`):**
    ```json
    [
      { "id": 1, "iataCode": "LIM", "name": "Aeropuerto Internacional Jorge Chávez", "city": "Lima", "country": "Perú", "timezone": "America/Lima" },
      { "id": 2, "iataCode": "CUZ", "name": "Aeropuerto Internacional Alejandro Velasco Astete", "city": "Cusco", "country": "Perú", "timezone": "America/Lima" }
    ]
    ```
* **`GET /api/v1/airports/{iataCode}`** (Público)
  - **Respuesta Esperada (`200 OK`):** Objeto único del aeropuerto solicitado (ej. `/api/v1/airports/LIM`).

---

### 4.3. Módulo de CRUD de Vuelos (`/api/v1/flights`) — Integrante 4
* **`GET /api/v1/flights/{flightId}`** (Público)
  - **Respuesta Esperada (`200 OK`):** Detalle completo del vuelo con objetos anidados de aerolínea y aeropuertos.
* **`POST /api/v1/flights`** (Requiere `ROLE_ADMIN`)
  - **Header:** `Authorization: Bearer <token_admin>`
  - **Body Enviado:**
    ```json
    { "flightNumber": "LA2050", "airlineId": 1, "originAirportId": 1, "destinationAirportId": 2, "departureTime": "2026-10-25T08:00:00Z", "arrivalTime": "2026-10-25T09:20:00Z", "basePrice": 65.50, "availableSeats": 80, "stopsCount": 0, "durationMinutes": 80, "status": "SCHEDULED", "gate": "14", "terminal": "T1" }
    ```
  - **Respuesta Esperada (`201 Created`):** Objeto creado con su UUID asignado.
* **`PUT /api/v1/flights/{flightId}`** (Requiere `ROLE_ADMIN`): Actualiza el vuelo (`200 OK`).
* **`DELETE /api/v1/flights/{flightId}`** (Requiere `ROLE_ADMIN`): Elimina el vuelo (`204 No Content`).

---

### 4.4. Módulo de Búsqueda y Recomendaciones (`/api/v1/flights`) — Integrante 5
* **`GET /api/v1/flights/search?origin=LIM&destination=CUZ&departureDate=2026-10-25&maxPrice=100&maxStops=0`** (Público)
  - **Respuesta Esperada (`200 OK`):**
    ```json
    [
      {
        "flightId": "f0000001-0000-0000-0000-000000000001",
        "flightNumber": "LA2040",
        "airline": { "id": 1, "iataCode": "LA", "name": "LATAM Airlines", "logoUrl": "/logos/latam.png" },
        "origin": { "id": 1, "iataCode": "LIM", "city": "Lima" },
        "destination": { "id": 2, "iataCode": "CUZ", "city": "Cusco" },
        "departureTime": "2026-10-25T08:30:00Z",
        "arrivalTime": "2026-10-25T09:55:00Z",
        "durationMinutes": 85,
        "stopsCount": 0,
        "basePrice": 64.99,
        "currency": "USD",
        "availableSeats": 45,
        "status": "SCHEDULED"
      }
    ]
    ```
* **`GET /api/v1/flights/popular-destinations`** (Público): Retorna lista de 6 vuelos destacados (`200 OK`).
* **`GET /api/v1/flights/ai-recommendation`** (Público): Retorna el vuelo con mejor relación costo-beneficio (`200 OK`).

---

### 4.5. Módulo de Comparación, Live Board y Mis Viajes — Integrante 6
* **`POST /api/v1/flights/compare`** (Público)
  - **Body Enviado:** `["f0000001-0000-0000-0000-000000000001", "f0000001-0000-0000-0000-000000000002"]`
  - **Respuesta Esperada (`200 OK`):**
    ```json
    {
      "flights": [...],
      "cheapestFlight": { "flightNumber": "H25102", "basePrice": 49.50 },
      "fastestFlight": { "flightNumber": "H25102", "durationMinutes": 80 },
      "averagePrice": 57.24,
      "priceDifference": 15.49
    }
    ```
* **`GET /api/v1/flight-status/live?airport=LIM&type=DEPARTURES`** (Público): Tablero FIDS con estados `BOARDING`, `DELAYED`, `SCHEDULED` (`200 OK`).
* **`GET /api/v1/flight-status/track/{flightNumber}`** (Público): Rastreo por código de vuelo ej. `/track/LA2040` (`200 OK`).
* **`GET /api/v1/trips`** (Requiere Token `ROLE_USER`): Vuelos guardados por el usuario actual (`200 OK`).
* **`POST /api/v1/trips`** (Requiere Token): Guarda vuelo en itinerario `{ "flightId": "uuid", "notes": "Vacaciones" }` (`201 Created`).

---

## 🔌 5. Matriz de Conexión Frontend <-> Backend (Asignación Directa)

Para cumplir el **Criterio 5 de la Rúbrica**, cada integrante responsable de un módulo debe probar su backend conectándolo con la vista correspondiente del Frontend:

| Integrante | Módulo / Pantalla Frontend | Archivo TypeScript a Conectar | Cómo Probar la Conexión Real |
| :--- | :--- | :--- | :--- |
| **Integrante 2** | Modal de Login y Registro (`/login`, `/register`) | [`FrontEnd/src/services/authService.ts`](file:///d:/Projects/VS_code/HERRAMIENTAS%20DESARROLLO/indexador-venta-vuelos/FrontEnd/src/services/authService.ts) | Cambiar `USE_MOCKS = false` en `authService.ts`. Iniciar sesión en el navegador con `demo@chasquifly.com` / `Password123!`. Comprobar que en la cabecera aparezca el nombre del usuario y el JWT se guarde en `localStorage`. |
| **Integrante 3** | Selectores de Origen y Destino (`HeroSection`) | [`FrontEnd/src/services/homeService.ts`](file:///d:/Projects/VS_code/HERRAMIENTAS%20DESARROLLO/indexador-venta-vuelos/FrontEnd/src/services/homeService.ts) (`getAirports`) | Cambiar `USE_MOCKS = false` en `homeService.ts`. Al abrir el selector de aeropuertos, deben aparecer las opciones reales provenientes de la base de datos de Supabase (LIM, CUZ, AQP). |
| **Integrante 5** | Resultados de Búsqueda de Vuelos (`/flights`) | [`FrontEnd/src/services/flightService.ts`](file:///d:/Projects/VS_code/HERRAMIENTAS%20DESARROLLO/indexador-venta-vuelos/FrontEnd/src/services/flightService.ts) (`searchFlights`) | En `flightService.ts`, descomentar la llamada real `axios.get('/api/v1/flights/search')`. Al dar clic en "Buscar Vuelos", las tarjetas deben renderizarse con los precios y aerolíneas de PostgreSQL. |
| **Integrante 6** | Vista de Comparación (`/compare`) y Tracker (`/tracker`) | `compareService.ts` y `trackerService.ts` | Al seleccionar 2 vuelos y presionar "Comparar", los cálculos de diferencia de precio y vuelo más veloz deben ser calculados por el Backend. En `/tracker`, ingresar `LA2040` y verificar estado real. |
| **Integrante 7 (Kevin)** | Conexión Global en Producción | [`FrontEnd/.env.production`](file:///d:/Projects/VS_code/HERRAMIENTAS%20DESARROLLO/indexador-venta-vuelos/FrontEnd/.env.production) | Configurar `VITE_API_BASE_URL=https://chasquifly-backend.onrender.com/api/v1`. Probar que la web desplegada en Vercel consulte al backend en Render sin bloqueos de CORS ni Mixed Content. |

---

## 🧪 6. Protocolo de Pruebas Pre-Push (Pre-Flight Checklist)

> [!CAUTION]
> **Antes de hacer `git push` o solicitar Pull Request a `dev`, CADA integrante debe ejecutar obligatoriamente esta lista de comprobación en su máquina local:**

```bash
# 1. Compilación Limpia de Maven (Debe salir BUILD SUCCESS sin advertencias críticas)
mvn clean compile

# 2. Ejecutar Pruebas Unitarias
mvn test

# 3. Arrancar la aplicación localmente
mvn spring-boot:run
```

### Checklist de Verificación Manual:
1. **Verificar Swagger UI:** Abrir [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) y probar que tus endpoints retornen códigos `200` o `201` con datos válidos.
2. **Verificar Base de Datos en Supabase:** En el editor de tablas de Supabase, comprobar que los datos que tu endpoint guarda (`INSERT` / `UPDATE`) se reflejen físicamente.
3. **Verificar que no tocaste archivos ajenos:** Ejecutar `git status` y verificar que **únicamente** modificaste los archivos asignados a tu rama en la Sección 3. Si modificaste un archivo compartido por error, descártalo con `git checkout <archivo>`.
4. **Formato de Commits:** Utilizar formato convencional:
   - `feat(auth): implement login and jwt token generation`
   - `feat(flights): add multi-criteria search endpoint with price filters`
   - `test(airports): add integration test for airport catalog`

---

## ⚠️ 7. Puntos Críticos: "Detalles que No Se Nos Pueden Escapar"

1. **Mixed Content (HTTPS en Producción):**
   - El frontend de Vercel está en `https://`. Si intenta llamar a un backend con `http://`, los navegadores modernos **bloquean la petición**.
   - *Solución implementada:* Render.com asigna automáticamente un certificado SSL gratuito (`https://chasquifly-backend.onrender.com`). En el frontend en producción **siempre** usar `https://`.
2. **Fechas Dinámicas en la Base de Datos:**
   - Si se insertaran fechas fijas como `2026-09-10`, al momento de exponer el vuelo ya habría caducado.
   - *Solución implementada:* La migración [`V2__seed_initial_data.sql`](file:///d:/Projects/VS_code/HERRAMIENTAS%20DESARROLLO/indexador-venta-vuelos/BackEnd/src/main/resources/db/migration/V2__seed_initial_data.sql) utiliza `NOW() + INTERVAL '1 day'`, garantizando que los vuelos **siempre tengan fechas futuras válidas** sin importar qué día califique el profesor.
3. **Pausa de Supabase por Inactividad:**
   - La capa gratuita de Supabase pausa instancias tras 7 días sin consultas.
   - *Prevención:* Entrar a Supabase 2 días antes de la sustentación para verificar que el proyecto esté activo ("Active").
4. **Resiliencia de Duffel API (Sandbox Expirable):**
   - Nunca confiar 100% en APIs externas durante una exposición académica. Si la red de la universidad bloquea Duffel o el sandbox vence, `DuffelClient.java` cuenta con fallback automático hacia PostgreSQL interno.

---

## 🎬 8. Guión de Demostración para la Sustentación del APF2 (10 Minutos)

Para asegurar la calificación máxima de 20 puntos en la sustentación:

1. **Minuto 1-2 (Arquitectura - Julio):** Mostrar diagrama de arquitectura (React -> Spring Boot 3 -> Supabase), destacar el uso de Spring Security con JWT Stateless y la configuración de CORS.
2. **Minuto 3-4 (Base de Datos & Flyway - Integrante 3):** Mostrar en pantalla Supabase y la tabla `flyway_schema_history` demostrando que la base de datos se versiona automáticamente con scripts `V1` y `V2`.
3. **Minuto 5-7 (Demostración de Endpoints en Swagger y Frontend - Int 2, 4, 5, 6):**
   - Integrante 2: Hacer login en Swagger y en el Frontend con el usuario de prueba, mostrando el token JWT emitido.
   - Integrante 5: Realizar una búsqueda real en el Frontend (`/flights`) demostrando que los datos vienen de la base de datos en Supabase.
   - Integrante 6: Mostrar la pantalla de comparación y el tablero de vuelos en vivo (`/tracker`).
4. **Minuto 8-9 (Despliegue en Render y Duffel - Kevin):** Mostrar el panel de Render.com con el servicio web en estado "Live", el endpoint `/actuator/health` respondiendo `{"status":"UP"}` y la arquitectura del cliente Duffel.
5. **Minuto 10 (GitFlow y Jira - Julio):** Mostrar el historial de commits limpios en GitHub organizados por ramas y el tablero Jira con todas las subtareas en estado "Done".

---

## 🌳 9. Flujo GitFlow y Reglas de Integración

```
main (Despliegue automático en Render.com / Vercel con Tag v2.0.0)
  ▲
  │ Pull Request final tras aprobación del Tech Lead
  │
dev (Rama de integración continua del Sprint 2)
  ▲
  ├── PR #1: feature/backend-base-security (Julio)
  ├── PR #2: feature/auth-user-module (Integrante 2)
  ├── PR #3: feature/database-flyway-airports (Integrante 3)
  ├── PR #4: feature/flight-search-crud (Integrante 4)
  ├── PR #5: feature/flight-search-engine (Integrante 5)
  ├── PR #6: feature/flight-compare-tracking (Integrante 6)
  └── PR #7: feature/duffel-devops-render (Kevin)
```

> [!IMPORTANT]
> **Reglas de Oro para los Integrantes:**
> 1. Toda rama debe crearse a partir de `dev`: `git checkout -b feature/<nombre-rama> dev`.
> 2. Cada integrante solo modifica sus **Archivos Exclusivos** asignados para evitar conflictos de merge.
> 3. Antes de solicitar Pull Request a `dev`, ejecutar `mvn clean compile` localmente para certificar cero errores de compilación.
