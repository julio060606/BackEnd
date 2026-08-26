# Sprint 3: CI/CD, Viajes y Motor de Notificaciones

- **Duración:** Semanas 12 a 15.
- **Hito de Evaluación:** **Avance de Proyecto Final 3 (APF3)** (Semana 15).
- **Objetivo:** Implementación del motor de seguimiento de viajes del usuario, CronJobs y Workers de proximidad de vuelo (alertas 24h y 3h), simulador de correo electrónico y automatización de integración continua (CI/CD con GitHub Actions).

---

## 👥 Asignación de Roles y Tareas (Sprint 3)

| Integrante / Rol | Foco Principal en Sprint 3 | Entregables Específicos |
| :--- | :--- | :--- |
| **Integrante 3 (Scrum Master S3)** | Gestión de Sprint 3 y pipeline CI/CD | Workflow de GitHub Actions (build, test, linter de Frontend y Backend) |
| **Integrante 1 (Backend Dev)** | Módulo de Viajes del Usuario (`Trips`) | Endpoints `/trips`, vinculación de vuelos al perfil y persistencia de notas |
| **Integrante 2 (Backend Dev)** | Motor de Schedulers y CronJobs | Scheduler `@Scheduled` para calcular ventanas de tiempo (24h y 3h antes de salida) |
| **Integrante 4 (Backend Dev)** | Worker de Despacho de Notificaciones | Procesador asíncrono `@Async` de la tabla `notifications_queue` y logs de correo |
| **Integrante 5 (Frontend Dev)** | UI de Notificaciones en Tiempo Real | Centro de notificaciones en el Navbar y vista detallada de alertas del usuario |
| **Integrante 6 (Frontend Dev)** | Experiencia de Monitoreo de Viajes | Tarjetas de viajes con cuenta regresiva en vivo e indicadores de estado |
| **Integrante 7 (QA / DevOps)** | Pruebas de Integración y Regresión | Pruebas de carga simulada y cobertura de pruebas automatizadas |

---

## 📋 Entregables Clave del Sprint 3 (APF3)
1. Sistema de seguimiento de vuelos personalizado activo para usuarios registrados.
2. Motor de notificaciones automático ejecutándose en background y simulando el despacho de correos según la proximidad del vuelo.
3. Alertas prioritarias automáticas en caso de demoras o cancelaciones.
4. Pipeline de CI/CD ejecutando pruebas en cada Pull Request a `develop` y `main`.
