---
name: duffel-integration
description: Pautas para la integración del cliente de la API de Duffel, manejo de sandbox, fallbacks y aislamiento de datos.
---

# Skill: Duffel API Integration & Fallback Strategy

Esta skill guía la implementación y consumo del proveedor de vuelos Duffel como complemento al catálogo local.

---

## 🎯 1. Principio de Aislamiento y Resiliencia
- La base de datos PostgreSQL local **es el motor primario** del sistema requerido por la rúbrica del curso (CRUDs, persistencia, Flyway).
- Duffel actúa como un **proveedor externo opcional** que enriquece la búsqueda de vuelos cuando está habilitado.
- Si el sandbox de Duffel expira, falla la red o la cuota de peticiones se agota, la API debe degradar graciosamente consultando la base de datos interna sin emitir excepciones 500 al frontend.

---

## ⚙️ 2. Configuración y Feature Flag
- Controlado por la propiedad: `app.duffel.enabled: ${DUFFEL_ENABLED:false}`
- En entornos locales o de testing donde no se disponga de token de Duffel, la propiedad permanece en `false`.
- El cliente `DuffelClient` valida esta bandera antes de ejecutar cualquier llamada HTTP.
