# 🛫 ChasquiFly - Backend REST API (Spring Boot 3 + Java 21)

Backend oficial del indexador y comparador de vuelos **ChasquiFly**, desarrollado para el **Avance de Proyecto Final 2 (APF2)**.

---

## 🏛️ Arquitectura y Tecnologías
- **Framework:** Spring Boot 3.3.4 (Java 21 LTS)
- **Base de Datos:** PostgreSQL en la nube (Supabase) con esquema relacional normalizado (3FN).
- **Control de Versiones BD:** Flyway (`V1__create_tables.sql`, `V2__seed_initial_data.sql`).
- **Seguridad:** Spring Security con autenticación Stateless mediante JWT (`jjwt 0.11.5`) y encriptación de contraseñas con `BCrypt` (work factor 10).
- **Documentación API:** OpenAPI 3 & Swagger UI (`springdoc-openapi 2.6.0`).
- **Proveedor Externo de Vuelos:** Cliente Duffel API en `com.chasquifly.client.duffel` con fallback resiliente y soporte de mocks.
- **Contenerización y Despliegue:** Docker multi-stage optimizado para Java 21 y blueprint `render.yaml` para despliegue en Render.com.

---

## 🚀 Inicio Rápido en Desarrollo Local

### 1. Prerrequisitos
- **Java 21 SDK** (ej. Eclipse Temurin 21)
- **Maven 3.9+**
- Instancia de PostgreSQL (local o remota en Supabase).

### 2. Configuración de Variables de Entorno
Crea o edita tu archivo `.env` en la raíz de `BackEnd/` con los placeholders correspondientes:
```env
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080
SPRING_DATASOURCE_URL=jdbc:postgresql://<TU_SUPABASE_HOST>:5432/postgres?sslmode=require
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=<TU_SUPABASE_PASSWORD>
JWT_SECRET=<TU_JWT_SECRET_64_HEX_CHARS>
CORS_ALLOWED_ORIGINS=http://localhost:5173,http://localhost:3000,https://chasquifly.vercel.app

# Integración Duffel API (Opcional)
DUFFEL_ENABLED=false
DUFFEL_API_TOKEN=<TU_DUFFEL_TOKEN_OPCIONAL>
APP_DUFFEL_MOCK_ENABLED=false
```

### 3. Compilación y Ejecución
```bash
# Compilar y ejecutar pruebas
mvn clean test

# Iniciar servidor local
mvn spring-boot:run
```
El servidor backend iniciará en el puerto `http://localhost:8080`.

---

## ✈️ Integración Duffel API (Proveedor Externo)

El módulo Duffel (`com.chasquifly.client.duffel`) permite enriquecer la búsqueda de vuelos con ofertas de aerolíneas globales a través de la API v2 de Duffel.

### Flags y Configuración

| Propiedad Spring | Variable de Entorno | Valor por Defecto | Descripción |
| :--- | :--- | :--- | :--- |
| `app.duffel.enabled` | `DUFFEL_ENABLED` | `false` | Activa (`true`) o desactiva (`false`) las peticiones a la API externa de Duffel. |
| `app.duffel.mock-enabled` | `APP_DUFFEL_MOCK_ENABLED` | `false` | Si es `true`, devuelve ofertas simuladas (LATAM, SKY, JetSMART) coherentes con la ruta y fecha sin llamadas HTTP (ideal para demos y desarrollo frontend). |
| `app.duffel.api-token` | `DUFFEL_API_TOKEN` | *vacío* | Token de autenticación Bearer de Duffel (`duffel_test_...` o live). |
| `app.duffel.base-url` | `APP_DUFFEL_BASE_URL` | `https://api.duffel.com` | URL base de la API de Duffel. |
| `app.duffel.api-version` | `APP_DUFFEL_API_VERSION` | `v2` | Versión del API enviada en la cabecera `Duffel-Version`. |
| `app.duffel.max-offers` | `APP_DUFFEL_MAX_OFFERS` | `10` | Límite máximo de ofertas a procesar por solicitud. |
| `app.duffel.connect-timeout-ms` | `APP_DUFFEL_CONNECT_TIMEOUT_MS` | `3000` | Timeout de conexión HTTP en milisegundos. |
| `app.duffel.read-timeout-ms` | `APP_DUFFEL_READ_TIMEOUT_MS` | `8000` | Timeout de lectura/respuesta HTTP en milisegundos. |

### Estrategia de Fallback y Resiliencia
- **Aislamiento:** El catálogo primario del sistema reside en PostgreSQL local/Supabase.
- **Degradación Elegante:** Si `app.duffel.enabled=false`, si el token no está configurado, o si la API de Duffel no responde (timeout, error 4xx/5xx, cuota agotada), `DuffelClient` captura cualquier error registrando una advertencia (`warn`) y retorna una lista vacía. **Nunca genera excepciones HTTP 500 al cliente**.

---

## ☁️ Despliegue en Render.com Paso a Paso

El backend está configurado para desplegarse como un **Web Service Docker** en Render.com, aprovechando el plan gratuito (512 MB RAM) con parámetros de JVM ajustados (`-XX:MaxRAMPercentage=75`).

### Método 1: Despliegue Automático mediante Blueprint (`render.yaml`)

1. Inicia sesión en [Render.com](https://dashboard.render.com).
2. Haz clic en **New +** y selecciona **Blueprint**.
3. Conecta el repositorio de GitHub `julio060606/BackEnd` (rama `feature/duffel-devops-render` o `dev`).
4. Render detectará automáticamente el archivo `render.yaml` en la raíz.
5. Completa las variables marcadas como secretas (`sync: false`):
   - `SPRING_DATASOURCE_URL`: URL JDBC de Supabase (con `?sslmode=require`).
   - `SPRING_DATASOURCE_USERNAME`: Usuario de Supabase (`postgres`).
   - `SPRING_DATASOURCE_PASSWORD`: Contraseña de la base de datos Supabase.
   - `DUFFEL_API_TOKEN`: (Opcional, dejar vacío si `DUFFEL_ENABLED=false`).
6. Haz clic en **Apply**. Render compilará la imagen Docker multi-stage y levantará el servicio.

### Método 2: Despliegue Manual como Web Service

1. En el Dashboard de Render, pulsa **New +** > **Web Service**.
2. Conecta tu repositorio de GitHub.
3. Configura los siguientes campos:
   - **Name:** `chasquifly-backend`
   - **Region:** `Oregon (US West)` o la más cercana a tu base de datos.
   - **Branch:** `feature/duffel-devops-render` (o `main`/`dev`).
   - **Root Directory:** *dejar vacío* (la raíz del repositorio es la raíz del proyecto).
   - **Runtime:** `Docker`.
   - **Dockerfile Path:** `./Dockerfile`.
   - **Docker Context:** `.`.
   - **Instance Type:** `Free`.
   - **Health Check Path:** `/actuator/health`.
4. En la sección **Environment Variables**, añade las variables descritas en la tabla inferior.
5. Pulsa **Create Web Service**.

### Tabla de Variables de Entorno para Producción (Render)

| Variable | Valor Recomendado | Secreto | Descripción |
| :--- | :--- | :---: | :--- |
| `SPRING_PROFILES_ACTIVE` | `prod` | No | Activa el perfil de configuración de producción. |
| `SERVER_PORT` | `8080` | No | Puerto interno del contenedor. |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://<HOST>:5432/postgres?sslmode=require` | Sí | URL de conexión a PostgreSQL / Supabase. |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Sí | Usuario de la base de datos. |
| `SPRING_DATASOURCE_PASSWORD` | `<CONTRASEÑA_SUPABASE>` | Sí | Contraseña de la base de datos. |
| `JWT_SECRET` | *Generado automáticamente por Render (64 hex)* | Sí | Clave secreta para firmar tokens JWT. |
| `CORS_ALLOWED_ORIGINS` | `https://chasquifly.vercel.app,http://localhost:5173` | No | Orígenes permitidos para peticiones CORS. |
| `DUFFEL_ENABLED` | `false` | No | Desactivado por defecto en plan free. |
| `DUFFEL_API_TOKEN` | `<TOKEN_DUFFEL>` | Sí | Token sandbox o live de Duffel (opcional). |

---

## 🌐 Conexión con el Frontend (Vercel / Local)

- **Frontend en Producción:** [https://chasquifly.vercel.app](https://chasquifly.vercel.app)
- **Repositorio Frontend:** [https://github.com/KevinZegarra/FrontEnd.git](https://github.com/KevinZegarra/FrontEnd.git)

En el frontend, la variable de entorno `VITE_API_BASE_URL` debe apuntar al Web Service de Render:
```env
VITE_API_BASE_URL=https://chasquifly-backend.onrender.com/api/v1
```

---

## 📖 Documentación Interactiva (Swagger UI)

Una vez iniciado el servicio:
- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- **Health Check:** [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)

---

## 👥 Integrantes y Responsabilidades

| Integrante | Rol | Responsabilidad |
| :--- | :--- | :--- |
| **Julio (Lead)** | Integrante 1 | Arquitectura base, Spring Security, JWT, CORS, GlobalExceptionHandler |
| **Integrante 2** | Integrante 2 | Módulo de usuarios, registro/login, roles y tokens |
| **Integrante 3** | Integrante 3 | Migraciones Flyway V1/V2, catálogos de aeropuertos y aerolíneas |
| **Integrante 4** | Integrante 4 | CRUD de vuelos, entidades JPA, persistencia relacional |
| **Integrante 5** | Integrante 5 | Motor de búsqueda multicriterio con filtros de fecha, precio y escalas |
| **Integrante 6** | Integrante 6 | Módulo de comparación, tablero FIDS en vivo y seguimiento |
| **Kevin (DevOps & External API)** | Integrante 7 | Cliente Duffel API, contenerización Docker, despliegue en Render y documentación |