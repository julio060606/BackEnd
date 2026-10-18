# 🛫 ChasquiFly - Backend REST API (Spring Boot 3 + Java 21)

Backend oficial del indexador y comparador de vuelos **ChasquiFly**, desarrollado para el **Avance de Proyecto Final 2 (APF2)**.

---

## 🏛️ Arquitectura y Tecnologías
- **Framework:** Spring Boot 3.3.4 (Java 21)
- **Base de Datos:** PostgreSQL en la nube (Supabase) con esquema relacional normalizado (3FN).
- **Control de Versiones BD:** Flyway (`V1__create_tables.sql`, `V2__seed_initial_data.sql`).
- **Seguridad:** Spring Security con autenticación Stateless mediante JWT (`jjwt 0.11.5`) y encriptación de contraseñas con `BCrypt` (work factor 10).
- **Documentación API:** OpenAPI 3 & Swagger UI (`springdoc-openapi 2.6.0`).
- **Proveedor Externo (Opcional):** Cliente para Duffel API en `client/duffel/`.
- **Despliegue:** Render.com (Docker multi-stage con Eclipse Temurin JDK 21).

---

## 🚀 Inicio Rápido en Desarrollo Local

### 1. Prerrequisitos
- **Java 21 SDK**
- **Maven 3.9+** (o ejecutar con `./mvnw` si está disponible)
- Acceso a base de datos PostgreSQL (local o instancia remota en Supabase).

### 2. Configuración de Variables de Entorno
Copie la plantilla de variables de entorno:
```bash
cp .env.example .env
```
O configure las siguientes variables en su IDE:
```env
SPRING_PROFILES_ACTIVE=dev
SPRING_DATASOURCE_URL=jdbc:postgresql://<SUPABASE_HOST>:5432/postgres?sslmode=require
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=dM5ZeFUXGCpZ4esm
JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
```

### 3. Compilación y Ejecución del Backend
```bash
mvn clean package -DskipTests
mvn spring-boot:run
```
El servidor backend iniciará en el puerto `http://localhost:8080`.

---

## 🌐 Conexión y Ejecución Conjunta con el Frontend

El Frontend de **ChasquiFly** (React + Vite + TypeScript) se encuentra alojado en su propio repositorio:
🔗 **Repositorio Frontend:** [https://github.com/KevinZegarra/FrontEnd.git](https://github.com/KevinZegarra/FrontEnd.git)

### Cómo ejecutar la solución completa (Full-Stack Local)

Para probar la aplicación de extremo a extremo, abra **dos terminales**:

#### Terminal 1: Backend (Spring Boot)
```bash
cd BackEnd
mvn spring-boot:run
# Backend listo en http://localhost:8080
```

#### Terminal 2: Frontend (React + Vite)
```bash
cd FrontEnd
npm install
npm run dev
# Frontend listo en http://localhost:5173
```

### ¿Cómo se comunican?
1. **CORS:** El backend ya tiene habilitado `http://localhost:5173` en su configuración de seguridad (`CorsConfig.java`). No habrá bloqueos en el navegador.
2. **Conexión de Endpoints:** Cada integrante, tras implementar su controlador en el backend, se dirige a la carpeta `FrontEnd/src/services/` de su respectivo módulo (ej. `authService.ts`, `flightService.ts`, `homeService.ts`), cambia `USE_MOCKS = false` y verifica que los datos provengan de la base de datos en Supabase.

---

## 📖 Documentación Interactiva (Swagger UI)
Una vez levantado el servidor local:
- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 👥 Asignación de Ramas del Sprint 2

| Integrante | Rama Git | Responsabilidad / US |
| :--- | :--- | :--- |
| **Integrante 1 (Julio - Lead)** | `feature/backend-base-security` | Arquitectura base, Spring Security, JWT, CORS, GlobalExceptionHandler |
| **Integrante 2** | `feature/auth-user-module` | Módulo de usuarios, registro/login, roles y tokens |
| **Integrante 3** | `feature/database-flyway-airports` | Migraciones Flyway V1/V2, Catálogos de aeropuertos y aerolíneas |
| **Integrante 4** | `feature/flight-search-crud` | CRUD de vuelos, entidades JPA, persistencia relacional |
| **Integrante 5** | `feature/flight-search-engine` | Motor de búsqueda multicriterio con filtros de fecha, precio y escalas |
| **Integrante 6** | `feature/flight-compare-tracking` | Módulo de comparación, tablero FIDS en vivo y seguimiento |
| **Integrante 7 (Kevin)** | `feature/duffel-devops-render` | Cliente Duffel API, despliegue en Render.com y documentación |