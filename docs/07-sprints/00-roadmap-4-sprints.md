# Roadmap Global del Proyecto (4 Sprints / Ciclo Completo)

Plan maestro de ejecución del proyecto **FlightTracker & Price Comparator** para el equipo de 7 integrantes, estructurado según las evaluaciones y cronograma del curso.

---

## 📅 Calendario de Sprints e Hitos de Evaluación

```
 SEMANA: 1   2   3   4   5   6 │ 7   8   9  10  11 │ 12  13  14  15 │ 16  17  18
 ──────────────────────────────┼───────────────────┼───────────────┼─────────────
 🏃 SPRINT 1: Frontend & Git   │ ⚙️ SPRINT 2: Core │ 🔔 SPRINT 3:  │ 🚀 SPRINT 4:
    UI/UX, MUI, Routing, Mock  │    Spring Boot,   │    CI/CD,     │    QA, Final
                               │    JPA, Postgres  │    Workers    │    Sustentación
 ──────────────────────────────┼───────────────────┼───────────────┼─────────────
 🎯 ENTREGA: APF1 (Semana 6)   │ 🎯 ENTREGA: APF2  │ 🎯 ENTREGA:   │ 🏆 PROYECTO
                               │    (Semana 11)    │    APF3 (S15) │    FINAL (S18)
```

---

## 🎯 Resumen de Objetivos por Sprint

### 🔵 Sprint 1 (Semanas 1 a 6) - Diseño Frontend y Bases de Control de Versiones
- **Hito:** **AVANCE DE PROYECTO FINAL 1 (APF1)**
- **Entregables:**
  - Repositorio y Git Flow configurado.
  - Interfaz de usuario completa y responsiva (MUI + React + TS).
  - Enrutamiento y validaciones con Zod.
  - Integración con Mock Data para pruebas visuales y de interacción.

### 🟢 Sprint 2 (Semanas 7 a 11) - Desarrollo Backend Core y Persistencia
- **Hito:** **AVANCE DE PROYECTO FINAL 2 (APF2)**
- **Entregables:**
  - Estructura base de Spring Boot con Maven y Java 17+.
  - Migraciones Flyway y conexión a PostgreSQL (Single-Tenant).
  - Endpoints REST para autenticación (JWT), aeropuertos y vuelos.
  - Conexión del Frontend con la API real.

### 🟡 Sprint 3 (Semanas 12 a 15) - CI/CD y Motor de Notificaciones
- **Hito:** **AVANCE DE PROYECTO FINAL 3 (APF3)**
- **Entregables:**
  - Implementación del módulo de seguimiento de viajes (`user_trips`).
  - Workers y CronJobs de proximidad (alertas 24h y 3h antes del vuelo).
  - Simulador de despacho de correos electrónicos.
  - Pipeline de CI/CD (GitHub Actions) con pruebas unitarias e integración continua.

### 🟣 Sprint 4 (Semanas 16 a 18) - Preparación para Despliegue y Sustentación
- **Hito:** **PROYECTO FINAL / SUSTENTACIÓN**
- **Entregables:**
  - Pruebas E2E y afinamiento de rendimiento.
  - Empaquetado agnóstico (JAR ejecutable y bundle de producción).
  - Documentación técnica final completa en `docs/`.
  - Presentación ejecutiva y demostración en vivo.
