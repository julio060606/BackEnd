# Estrategia de Migraciones y Versionado de Base de Datos con Flyway

Para asegurar que los 7 integrantes del equipo mantengan exactamente el mismo esquema de base de datos sin discrepancias manuales, el proyecto utiliza **Flyway** integrado en Spring Boot.

---

## 📁 Ubicación y Convención de Nomenclatura
Las migraciones se almacenan en:
`BackEnd/src/main/resources/db/migration/`

Patrón obligatorio:
`V<Version>__<descripcion_en_snake_case>.sql`

---

## 🗺️ Mapa de Scripts de Migración Actuales
 
| Archivo de Migración | Estado | Descripción |
| :--- | :--- | :--- |
| `V1__create_tables.sql` | Implementado | DDL completo: extensiones pgcrypto, tablas `users`, `airports`, `airlines`, `flights`, `user_trips`, `notifications_queue` e índices. |
| `V2__seed_initial_data.sql` | Implementado | Carga inicial de usuarios administradores, catálogo de 7 aeropuertos comerciales en Perú (IATA), aerolíneas y vuelos representativos. |

---

## 🛠️ Procedimiento de Verificación Local (Módulo Aeropuertos)
1. Iniciar el servicio PostgreSQL con la base de datos `chasquifly`.
2. Ejecutar la aplicación Spring Boot: `./mvnw spring-boot:run`. Flyway aplicará automáticamente `V1` y `V2`.
3. Consultar el endpoint público: `GET http://localhost:8080/api/v1/airports`.
4. Consultar un aeropuerto específico por código IATA: `GET http://localhost:8080/api/v1/airports/LIM`.
5. Verificar respuesta HTTP 200 con el payload JSON correspondiente o HTTP 404 para códigos inexistentes.

---

## ⚠️ Reglas Inmutables para el Equipo
1. **Nunca editar un archivo de migración ya ejecutado:** Si se requiere un cambio en una tabla, se debe crear un nuevo script `V<N+1>__...sql`.
2. **Idempotencia y Transaccionalidad:** Cada script debe ser auto-contenido y ejecutable de forma atómica.
3. **No usar DDL automático de Hibernate:** `spring.jpa.hibernate.ddl-auto` se configura estrictamente en `validate` para que Flyway sea la única fuente de verdad sobre la base de datos.
