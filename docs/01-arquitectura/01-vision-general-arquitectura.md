# Visión General de la Arquitectura del Sistema

El sistema **FlightTracker & Price Comparator** es una plataforma desacoplada cliente-servidor construida con tecnologías modernas y principios de ingeniería de software robustos.

---

## 🏛️ Topología de la Solución

```
                    ┌─────────────────────────────────────────┐
                    │            CLIENTE (SPA)                │
                    │   React 18 + TypeScript + Vite + MUI    │
                    │      React Hook Form + Zod + Axios      │
                    └────────────────────┬────────────────────┘
                                         │ HTTPS / REST JSON
                                         ▼
                    ┌─────────────────────────────────────────┐
                    │           BACKEND (API REST)            │
                    │        Spring Boot 3.x (Java 17+)       │
                    │   Spring Security (JWT Stateless)       │
                    │  Flyway (Migraciones) + Spring Data JPA │
                    │      Workers / Task Scheduler (@Async)  │
                    └───────────┬───────────────────┬─────────┘
                                │                   │
       ┌────────────────────────▼────────┐ ┌────────▼────────────────────────┐
       │     BASE DE DATOS RELACIONAL    │ │           CACHÉ (OPCIONAL)       │
       │     PostgreSQL (Single-Tenant)  │ │      Redis (@ConditionalOnProp)  │
       │  Esquemas, Índices y Triggers   │ │       [Desactivado por defecto]  │
       └─────────────────────────────────┘ └──────────────────────────────────┘
```

---

## 🧩 Capas del Backend (Spring Boot)

1. **Capa de Controladores (`controllers`):** Endpoints REST que reciben peticiones, aplican validaciones de entrada (`@Valid`) y retornan respuestas con códigos HTTP semánticos.
2. **Capa de Servicios (`services`):** Lógica de negocio pura, cálculo de comparaciones de vuelos, orquestación de notificaciones y transaccionalidad (`@Transactional`).
3. **Capa de Repositorios (`repositories`):** Acceso a datos con Spring Data JPA y consultas optimizadas mediante índices en PostgreSQL.
4. **Capa de Seguridad (`security`):** Filtro `JwtAuthenticationFilter`, manejo de tokens de acceso y refresco, encoder BCrypt.
5. **Capa de Tareas Programadas (`schedulers` / `workers`):** CronJobs con `@Scheduled` para evaluar periódicamente la proximidad de vuelos (24h y 3h) y despachar alertas a la cola de notificaciones.

---

## 🎨 Capas del Frontend (React + TypeScript)

1. **`src/components/common`:** Componentes atómicos y moleculares reutilizables (Botones, Modales, Cards, Chips, Alerts).
2. **`src/components/layout`:** Navbar, Sidebar/Drawer, Footer y Layout principal de la aplicación.
3. **`src/modules/`:** Módulos funcionales de la plataforma (`auth`, `flights`, `live-status`, `trips`, `alerts`). Cada módulo contiene sus vistas, componentes específicos, hooks y servicios.
4. **`src/services/`:** Cliente HTTP Axios configurado con interceptores para inyección de JWT y manejo automático de refresco de tokens.
5. **`src/validations/`:** Esquemas de validación Zod reutilizables para todos los formularios.
6. **`src/theme/`:** Tema personalizado de Material UI (MUI Theme Provider) con paleta de colores aeronáutica.

---

## 🔒 Principios de Diseño
- **Single-Tenant Estricto:** La base de datos es normalizada para la plataforma única, sin particiones multi-tenant.
- **Security by Design:** Protección contra XSS, CSRF, SQL Injection y validaciones en ambas capas.
- **KISS & Clean Code:** Evitar complejidad innecesaria para asegurar un desarrollo fluido entre los 7 integrantes del equipo.
