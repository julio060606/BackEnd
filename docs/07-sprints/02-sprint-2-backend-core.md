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
| **5. Integración Frontend** | 5 | El Frontend React debe consumir los endpoints reales del backend. | Consumo de `/api/v1/auth`, `/api/v1/flights` y `/api/v1/airports` alternando `VITE_USE_MOCKS=false`. | Asignado por módulo (Ver Sección 4) |
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
   - *Decisión técnica:* La base de datos es 100% en la nube (Single-Tenant, 3FN). La contraseña (`dM5ZeFUXGCpZ4esm`) se gestiona mediante variables de entorno (`SPRING_DATASOURCE_PASSWORD`), evitando exponerla en commits de Git y garantizando persistencia permanente (a diferencia del PostgreSQL de Render que se borra a los 30 días).

---

## 📖 3. Catálogo Guiado de Endpoints REST (Contratos Exactos)

Todos los endpoints tienen como prefijo base `/api/v1`.

### 3.1. Módulo de Autenticación (`/api/v1/auth`) — Integrante 2
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

### 3.2. Módulo de Aeropuertos y Catálogos (`/api/v1/airports`) — Integrante 3
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

### 3.3. Módulo de CRUD de Vuelos (`/api/v1/flights`) — Integrante 4
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

### 3.4. Módulo de Búsqueda y Recomendaciones (`/api/v1/flights`) — Integrante 5
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

### 3.5. Módulo de Comparación, Live Board y Mis Viajes — Integrante 6
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

## 🔌 4. Matriz de Conexión Frontend <-> Backend (Asignación Directa)

Para cumplir el **Criterio 5 de la Rúbrica**, cada integrante responsable de un módulo debe probar su backend conectándolo con la vista correspondiente del Frontend:

| Integrante | Módulo / Pantalla Frontend | Archivo TypeScript a Conectar | Cómo Probar la Conexión Real |
| :--- | :--- | :--- | :--- |
| **Integrante 2** | Modal de Login y Registro (`/login`, `/register`) | [`FrontEnd/src/services/authService.ts`](file:///d:/Projects/VS_code/HERRAMIENTAS%20DESARROLLO/indexador-venta-vuelos/FrontEnd/src/services/authService.ts) | Cambiar `USE_MOCKS = false` en `authService.ts`. Iniciar sesión en el navegador con `demo@chasquifly.com` / `Password123!`. Comprobar que en la cabecera aparezca el nombre del usuario y el JWT se guarde en `localStorage`. |
| **Integrante 3** | Selectores de Origen y Destino (`HeroSection`) | [`FrontEnd/src/services/homeService.ts`](file:///d:/Projects/VS_code/HERRAMIENTAS%20DESARROLLO/indexador-venta-vuelos/FrontEnd/src/services/homeService.ts) (`getAirports`) | Cambiar `USE_MOCKS = false` en `homeService.ts`. Al abrir el selector de aeropuertos, deben aparecer las opciones reales provenientes de la base de datos de Supabase (LIM, CUZ, AQP). |
| **Integrante 5** | Resultados de Búsqueda de Vuelos (`/flights`) | [`FrontEnd/src/services/flightService.ts`](file:///d:/Projects/VS_code/HERRAMIENTAS%20DESARROLLO/indexador-venta-vuelos/FrontEnd/src/services/flightService.ts) (`searchFlights`) | En `flightService.ts`, descomentar la llamada real `axios.get('/api/v1/flights/search')`. Al dar clic en "Buscar Vuelos", las tarjetas deben renderizarse con los precios y aerolíneas de PostgreSQL. |
| **Integrante 6** | Vista de Comparación (`/compare`) y Tracker (`/tracker`) | `compareService.ts` y `trackerService.ts` | Al seleccionar 2 vuelos y presionar "Comparar", los cálculos de diferencia de precio y vuelo más veloz deben ser calculados por el Backend. En `/tracker`, ingresar `LA2040` y verificar estado real. |
| **Integrante 7 (Kevin)** | Conexión Global en Producción | [`FrontEnd/.env.production`](file:///d:/Projects/VS_code/HERRAMIENTAS%20DESARROLLO/indexador-venta-vuelos/FrontEnd/.env.production) | Configurar `VITE_API_BASE_URL=https://chasquifly-backend.onrender.com/api/v1`. Probar que la web desplegada en Vercel consulte al backend en Render sin bloqueos de CORS ni Mixed Content. |

---

## 🧪 5. Protocolo de Pruebas Pre-Push (Pre-Flight Checklist)

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
3. **Verificar que no tocaste archivos ajenos:** Ejecutar `git status` y verificar que **únicamente** modificaste los archivos asignados a tu rama en la Sección 6. Si modificaste un archivo compartido por error, descártalo con `git checkout <archivo>`.
4. **Formato de Commits:** Utilizar formato convencional:
   - `feat(auth): implement login and jwt token generation`
   - `feat(flights): add multi-criteria search endpoint with price filters`
   - `test(airports): add integration test for airport catalog`

---

## ⚠️ 6. Puntos Críticos: "Detalles que No Se Nos Pueden Escapar"

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

## 🎬 7. Guión de Demostración para la Sustentación del APF2 (10 Minutos)

Para asegurar la calificación máxima de 20 puntos en la sustentación:

1. **Minuto 1-2 (Arquitectura - Julio):** Mostrar diagrama de arquitectura (React -> Spring Boot 3 -> Supabase), destacar el uso de Spring Security con JWT Stateless y la configuración de CORS.
2. **Minuto 3-4 (Base de Datos & Flyway - Integrante 3):** Mostrar en pantalla Supabase y la tabla `flyway_schema_history` demostrando que la base de datos se versiona automáticamente con scripts `V1` y `V2`.
3. **Minuto 5-7 (Demostración de Endpoints en Swagger y Frontend - Int 2, 4, 5, 6):**
   - Integrante 2: Hacer login en Swagger y en el Frontend con el usuario de prueba, mostrando el token JWT emitido.
   - Integrante 5: Realizar una búsqueda real en el Frontend (`/flights`) demostrando que los datos vienen de la base de datos en Supabase.
   - Integrante 6: Mostrar la pantalla de comparación y el tablero de vuelos en vivo (`/tracker`).
4. **Minuto 8-9 (Despliegue en Render y Duffel - Kevin):** Mostrar el panel de Render.com con el servicio web en estado "Live", el endpoint `/actuator/health` respondiendo `{"status":"UP"}` y la arquitectura del cliente Duffel.
5. **Minuto 10 (GitFlow y Jira - Julio):** Mostrar el historial de commits limpios en GitHub organizados por ramas y el tablero Jira con todas las subtareas en estado "Done".
