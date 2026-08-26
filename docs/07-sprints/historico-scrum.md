# Histórico Scrum, Acuerdos de Equipo y Gestión de Impedimentos

Este documento registra los acuerdos metodológicos, la rotación de roles y la gestión de impedimentos a lo largo de los 4 Sprints del proyecto.

---

## 🤝 Acuerdos de Trabajo del Equipo (7 Integrantes)

1. **Estrategia de Ramas (Git Flow Simplificado):**
   - `main`: Rama de producción / entregas oficiales (APF1, APF2, APF3, Final).
   - `develop`: Rama de integración activa donde convergen todas las características del sprint.
   - `feature/<modulo>-<descripcion>`: Ramas de trabajo individuales (ej. `feature/flight-search-bar`, `feature/auth-jwt-service`).
   - Todo merge a `develop` requiere Pull Request (PR) y revisión de al menos un compañero.
2. **Rotación Oficial de Scrum Master:**
   - **Sprint 1 (Semanas 1-6):** Integrante 1
   - **Sprint 2 (Semanas 7-11):** Integrante 2
   - **Sprint 3 (Semanas 12-15):** Integrante 3
   - **Sprint 4 (Semanas 16-18):** Integrante 4
3. **Mantenimiento Documental y Trabajo con IA:**
   - Todo cambio arquitectónico o de modelo de datos debe reflejarse inmediatamente en la subcarpeta correspondiente de `docs/`.
   - Cada asistente de IA asignado a un compañero debe consultar `docs/agentes-ia-guia.md` antes de iniciar cualquier desarrollo.

---

## 🛑 Bitácora de Impedimentos y Soluciones

| Sprint | Impedimento Identificado | Impacto | Decisión / Solución Aplicada | Estado |
| :--- | :--- | :--- | :--- | :---: |
| Sprint 1 | Heterogeneidad de entornos locales para Redis y Docker | Posibles fallos de compilación entre integrantes | Se define modo agnóstico para Docker (standby) y Redis bajo Feature Flag `@ConditionalOnProperty`. | Resuelto |
| Sprint 1 | Necesidad de avanzar Frontend antes de tener la API REST | Bloqueo en la construcción de pantallas | Se implementan contratos DTO estandarizados y Mock Data estructurada con TanStack Query. | Resuelto |
| Sprint 2 | Riesgo de desalineación en esquemas de BD entre los 7 integrantes | Inconsistencia de tablas y datos | Uso estricto de Flyway (`V1__...sql`) como única fuente de verdad DDL. | Planificado |
