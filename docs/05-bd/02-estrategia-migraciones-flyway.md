# Estrategia de Migraciones y Versionado de Base de Datos con Flyway

Para asegurar que los 7 integrantes del equipo mantengan exactamente el mismo esquema de base de datos sin discrepancias manuales, el proyecto utiliza **Flyway** integrado en Spring Boot.

---

## 📁 Ubicación y Convención de Nomenclatura
Las migraciones se almacenan en:
`BackEnd/src/main/resources/db/migration/`

Patrón obligatorio:
`V<Version>__<descripcion_en_snake_case>.sql`

---

## 🗺️ Mapa de Scripts de Migración Previstos

| Archivo de Migración | Sprint | Descripción |
| :--- | :--- | :--- |
| `V1__init_schema_airports_and_airlines.sql` | Sprint 2 | Creación de tablas de aeropuertos, aerolíneas e inserción de catálogos base. |
| `V2__init_schema_users_and_auth.sql` | Sprint 2 | Creación de tabla `users` y roles iniciales. |
| `V3__init_schema_flights.sql` | Sprint 2 | Creación de tabla `flights`, constraints e índices de búsqueda. |
| `V4__init_schema_trips_and_notifications.sql` | Sprint 3 | Creación de tablas `user_trips`, `notifications_queue` e índices. |
| `V5__seed_initial_flight_data.sql` | Sprint 2/3 | Carga de datos de prueba para vuelos nacionales e internacionales. |

---

## ⚠️ Reglas Inmutables para el Equipo
1. **Nunca editar un archivo de migración ya ejecutado:** Si se requiere un cambio en una tabla, se debe crear un nuevo script `V<N+1>__...sql`.
2. **Idempotencia y Transaccionalidad:** Cada script debe ser auto-contenido y ejecutable de forma atómica.
3. **No usar DDL automático de Hibernate:** `spring.jpa.hibernate.ddl-auto` se configura estrictamente en `validate` para que Flyway sea la única fuente de verdad sobre la base de datos.
