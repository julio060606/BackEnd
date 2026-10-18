---
name: flyway-database
description: Guía y directivas para versionamiento de esquemas, migraciones Flyway y conexión a Supabase PostgreSQL.
---

# Skill: Flyway & Supabase PostgreSQL Migration Directives

Esta skill define cómo gestionar las migraciones de base de datos relacional para el sistema Single-Tenant ChasquiFly.

---

## 📌 1. Principio Fundamental
- **Flyway es la única fuente de verdad** del esquema de base de datos.
- `spring.jpa.hibernate.ddl-auto` se configura estrictamente en `validate`.
- **Nunca** alterar tablas directamente en la consola de Supabase sin crear un archivo `.sql` versionado en `src/main/resources/db/migration/`.

---

## 🏷️ 2. Convención de Nombres de Migración
- Formato: `V<Version>__<descripcion_en_snake_case>.sql`
- Separador: Doble guion bajo `__`.
- Ejemplos existentes:
  - `V1__create_tables.sql`: Tablas base (`users`, `airports`, `airlines`, `flights`, `user_trips`, `notifications_queue`) e índices.
  - `V2__seed_initial_data.sql`: Datos maestros iniciales de aeropuertos peruanos, aerolíneas y vuelos representativos.
  - Futuras adiciones: `V3__add_flight_amenities.sql`, etc.

---

## ⚡ 3. Conexión con Supabase
- **Modo SSL:** Obligatorio (`?sslmode=require`).
- **Connection Pooler:** Si se utiliza el Transaction Pooler de Supabase (puerto `6543`), deshabilitar el caching de sentencias preparadas en el driver JDBC si se presentan inconsistencias (`&prepareThreshold=0`).
- **Credenciales Seguras:** Nunca commitear la contraseña de la base de datos en Git. Debe inyectarse mediante la variable de entorno `SPRING_DATASOURCE_PASSWORD`.
