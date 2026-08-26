# Contexto Maestro Del Proyecto: FlightTracker & Price Comparator

## Identidad Del Proyecto
- **Rol:** Arquitecto de Software Senior, Tech Lead, Ingeniero Full Stack y Especialista en Sistemas.
- **Misión:** Diseñar, documentar y programar un sistema indexador y comparador de vuelos (precios, estado, cancelaciones, seguimiento inteligente y alertas por proximidad de viaje).
- **Entorno Académico:** Curso de Herramientas de Desarrollo. Estándares profesionales, trabajo en equipo (7 integrantes) y pragmatismo técnico.
- **Principios de Código:** Clean Code, SOLID, DRY, KISS, Security by Design.

---

## Objetivo General
Desarrollar una plataforma integral en la que un usuario pueda registrarse, comparar opciones de vuelos (precios, disponibilidad, estado), y configurar un sistema de notificaciones personalizadas que le avise sobre el estado de su vuelo y proximidad del viaje mediante correo electrónico (u otros medios de mensajería).

---

## Organización del Equipo y Metodología (Scrum / 4 Sprints)
Equipo de **7 integrantes** con rotación de roles (1 Scrum Master por Sprint):

1. **Sprint 1 (Semanas 1 a 6) - Diseño Frontend y Bases de Control de Versiones:**
   - Enfoque: UI/UX, componentes reutilizables, routing, validaciones base.
   - Hito: **AVANCE DE PROYECTO FINAL 1 (APF1)** - Semana 6.
2. **Sprint 2 (Semanas 7 a 11) - Desarrollo Backend Core y Colaboración:**
   - Enfoque: Spring Boot, conexión PostgreSQL, endpoints core.
   - Hito: **AVANCE DE PROYECTO FINAL 2 (APF2)** - Semana 11.
3. **Sprint 3 (Semanas 12 a 15) - CI/CD y Notificaciones:**
   - Enfoque: Pipelines, Workers / CronJobs para proximidad y alertas.
   - Hito: **AVANCE DE PROYECTO FINAL 3 (APF3)** - Semana 15.
4. **Sprint 4 (Semanas 16 a 18) - Preparación para Despliegue:**
   - Enfoque: QA, afinamiento de empaquetado y sustentación.
   - Hito: **PROYECTO FINAL** - Semana 18.

---

## Stack Tecnológico y Arquitectura

### Frontend
- **Framework / Bundler:** React + TypeScript + Vite.
- **Enrutamiento y Estado Servidor:** React Router v6+, TanStack Query (React Query).
- **Formularios y Validación:** React Hook Form + Zod.
- **Librería UI:** Material UI (MUI).

### Backend
- **Framework:** Spring Boot (Java).
- **Seguridad:** Spring Security, JWT (Access & Refresh Tokens).
- **Persistencia:** PostgreSQL con JPA / Hibernate y migraciones con Flyway.
- **Documentación API:** OpenAPI / Swagger (springdoc-openapi).
- **Gestión de dependencias:** Maven.

### Base de Datos
- **Motor:** PostgreSQL.
- **Arquitectura de Datos:** **Single-Tenant estricto** (Multi-tenant totalmente descartado). Esquema relacional optimizado por dominios funcionales e índices.

---

## Decisiones Técnicas y de Infraestructura (Reglas Estrictas)

1. **Docker (Modo Pausado / Agnóstico):**
   - No se utiliza Docker en el desarrollo diario por practicidad académica.
   - Archivos `Dockerfile` (frontend y backend) y `docker-compose.yml` en la raíz se mantendrán mínimos/comentados como placeholders agnósticos.
2. **Redis (Modo Feature Flag / Pausado):**
   - El código se estructura con soporte para Redis pero deshabilitado por defecto.
   - Configuración: `app.cache.enabled: false` en `application.yml`.
   - Uso obligatorio de `@ConditionalOnProperty(name = "app.cache.enabled", havingValue = "true")`. Si está en `false`, se consulta directamente a PostgreSQL.

---

## Seguridad (Security by Design)
- Autenticación stateless con JWT y Refresh Token.
- Hashing seguro de contraseñas (BCrypt).
- Prevención de CSRF, XSS y SQL Injection (vía ORM parametrizado).
- Rate Limiting en endpoints públicos/sensibles.
- Validación simétrica de datos (Frontend con Zod, Backend con Bean Validation `@Valid`).

---

## Módulos del Sistema
- **Autenticación y Usuarios:** Registro, login, perfil y recuperación.
- **Búsqueda y Comparación:** Motor de filtros por aerolínea, fechas, precios y escalas.
- **Estado de Vuelos:** Tablero en tiempo real sobre cancelaciones, embarques y demoras.
- **Seguimiento e Historial (Trips):** Vuelos seguidos o adquiridos por el usuario.
- **Notificaciones (CronJobs/Workers):** Cálculo de proximidad y despacho de alertas simuladas por correo.

---

## Estructura Documental Modular (`docs/`)
- `docs/00-contexto-maestro.md` (Este archivo)
- `docs/README.md` (Índice general)
- `docs/agentes-ia-guia.md` (Guía de contexto para IAs)
- `docs/sprints/` (Bitácora de progreso y acuerdos Scrum)
- `docs/ui-ux/` (Guía de diseño, MUI, Zod)
- `docs/arquitectura/` (ADRs e infraestructura)
- `docs/api/` (Contratos de endpoints y payloads)
- `docs/bd/` (Diccionario de datos y esquemas)
- `docs/04-modulos/` (Especificaciones de negocio por módulo)
- `docs/seguridad/` (Políticas de auth, JWT y permisos)
