# Módulo 03: Tablero en Vivo de Estado de Vuelos (Live Board)

Permite consultar en tiempo real las operaciones aeroportuarias, estados de embarque, cancelaciones y demoras.

---

## 🎯 Alcance Funcional
1. **Tablero de Salidas y Llegadas (FIDS - Flight Information Display System):**
   - Vista tabular con actualización de estatus.
   - Pestañas para alternar entre "Salidas" y "Llegadas".
2. **Buscador de Vuelo Individual:**
   - Consulta rápida por número comercial de vuelo (ej. `LA2041`).
   - Muestra detalles operativos: puerta de embarque (`Gate`), terminal, tiempo estimado de demora.
3. **Estados Soportados:**
   - `SCHEDULED` (A tiempo / Programado).
   - `BOARDING` (Embarcando / Puerta Abierta).
   - `DELAYED` (Demorado - muestra nueva hora y minutos de retraso).
   - `CANCELLED` (Cancelado - alerta destacada en rojo).
   - `LANDED` (Aterrizado / Completado).

---

## ⚡ Reglas de Negocio
- Los vuelos cancelados deben alertar inmediatamente al usuario en la interfaz visual con un chip distintivo.
- Si un vuelo cambia a `CANCELLED` o `DELAYED` (>30 min) y tiene usuarios suscritos a alertas, se encola un evento prioritario de notificación.
