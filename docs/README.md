# Índice General de Documentación - FlightTracker & Price Comparator

Bienvenido a la base de conocimiento y arquitectura global del proyecto **FlightTracker & Price Comparator**.
Este repositorio contiene la especificación completa del sistema a lo largo de sus **4 Sprints** de desarrollo, diseñada para el equipo de 7 integrantes y los asistentes de IA.

---

## 📂 Estructura Numérica de Documentación

```
docs/
├── 00-contexto-maestro.md                    # Fuente de verdad global del proyecto
├── agentes-ia-guia.md                        # Guía de consulta rápida y reglas para IAs
├── README.md                                 # Este índice general
│
├── 01-arquitectura/                          # Arquitectura global, componentes y decisiones
│   ├── 01-vision-general-arquitectura.md     # Topología cliente-servidor, capas y principios
│   └── 02-decisiones-arquitectura-adr.md     # ADRs: Single-tenant, Docker pausado, Redis condicional
│
├── 02-ui-ux/                                 # Sistema de diseño, componentes y validaciones
│   ├── 01-sistema-diseno-guia-estilos.md     # Paleta aeronáutica, tipografía, layouts y MUI
│   └── 02-estandares-formularios-zod.md      # Esquemas de validación simétricos con Zod
│
├── 03-api/                                   # Especificación de contratos e interfaces REST
│   ├── 01-contratos-endpoints.md             # Catálogo completo de endpoints para toda la plataforma
│   └── 02-manejo-errores-y-respuestas.md     # Formato estándar de errores (RFC 7807) y excepciones
│
├── 04-modulos/                               # Especificaciones funcionales de negocio
│   ├── 01-autenticacion-y-usuarios.md        # Registro, login, refresh token y perfiles
│   ├── 02-busqueda-y-comparacion-vuelos.md   # Filtros, fechas, escalas, aerolíneas y precios
│   ├── 03-estado-vuelos-live-board.md        # Tablero en vivo de salidas/llegadas y cancelaciones
│   ├── 04-mis-vuelos-y-seguimiento-trips.md  # Historial de viajes guardados y compras
│   └── 05-notificaciones-y-alertas.md        # CronJobs de proximidad (24h/3h) y simulador de correo
│
├── 05-bd/                                    # Persistencia y modelo de datos relacional
│   ├── 01-diccionario-datos-postgresql.md    # DDL, tablas, claves foráneas e índices optimizados
│   └── 02-estrategia-migraciones-flyway.md   # Versionado de base de datos V1..Vn y buenas prácticas
│
├── 06-seguridad/                             # Security by Design
│   ├── 01-politicas-seguridad-y-jwt.md       # Stateless JWT, BCrypt, Rate Limiting, CORS/CSRF/XSS
│   └── 02-control-acceso-rbac.md             # Matriz de roles (ROLE_USER, ROLE_ADMIN) y permisos
│
└── 07-sprints/                               # Gestión Ágil del proyecto (4 Sprints / Curso)
    ├── 00-roadmap-4-sprints.md               # Visión global del cronograma y entregables APF1 a APF4
    ├── 01-sprint-1-frontend.md               # Semanas 1 a 6 (APF1) - UI/UX y control de versiones
    ├── 02-sprint-2-backend-core.md           # Semanas 7 a 11 (APF2) - Spring Boot & PostgreSQL
    ├── 03-sprint-3-cicd-notificaciones.md    # Semanas 12 a 15 (APF3) - Workers & CI/CD
    ├── 04-sprint-4-despliegue.md             # Semanas 16 a 18 - QA, empaquetado y sustentación
    └── historico-scrum.md                    # Bitácora de acuerdos, rotación y bloqueos
```

---

## 🗺️ Mapa de Fases del Proyecto

| Fase / Sprint | Semanas | Enfoque Principal | Entregable Clave |
| :--- | :--- | :--- | :--- |
| **Sprint 1** | Semanas 1 - 6 | Diseño UI/UX, Componentes React, Routing y Mock Data | **APF1 (Avance 1)** |
| **Sprint 2** | Semanas 7 - 11 | Backend Spring Boot, JPA, Flyway, PostgreSQL | **APF2 (Avance 2)** |
| **Sprint 3** | Semanas 12 - 15 | CronJobs de proximidad de vuelos, Alertas, CI/CD | **APF3 (Avance 3)** |
| **Sprint 4** | Semanas 16 - 18 | Empaquetado agnóstico, QA integral y Sustentación | **PROYECTO FINAL** |
