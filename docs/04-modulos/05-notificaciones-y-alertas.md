# Módulo 05: Motor de Notificaciones y Alertas de Proximidad

Este módulo implementa el scheduler y los workers encargados de vigilar la proximidad temporal de los viajes y despachar notificaciones simuladas o reales a los usuarios.

---

## 🎯 Alcance Funcional
1. **Evaluador Periódico de Proximidad (Scheduler / CronJob):**
   - Tarea programada en Spring Boot (`@Scheduled(cron = "0 */5 * * * *")` - cada 5 minutos).
   - Busca en la base de datos vuelos asociados a viajes activos que cumplan con ventanas de tiempo clave:
     - **Ventana 24 Horas:** Vuelos entre 24h y 23h55m antes del despegue (Check-in abierto).
     - **Ventana 3 Horas:** Vuelos entre 3h y 2h55m antes del despegue (Salida hacia el aeropuerto y puerta de embarque).
2. **Alertas de Evento Inmediato:**
   - Si un vuelo cambia de estado a `CANCELLED` o `DELAYED` (>30 min), se genera una alerta instantánea fuera del ciclo regular de proximidad.
3. **Cola de Despacho (Queue & Dispatcher):**
   - Las notificaciones se registran en la tabla `notifications_queue` con estado `PENDING`.
   - Un worker asíncrono (`@Async`) procesa la cola, marca la notificación como `SENT` y genera el log formateado de correo simulado en consola/BD.
4. **Bandeja de Notificaciones en Frontend:**
   - Dropdown o vista en la barra de navegación del usuario con el historial de alertas recibidas y contador de no leídas.

---

## ⚡ Formato del Mensaje Simulado de Correo

```
==================== CORREO ELECTRÓNICO (SIMULACIÓN) ====================
Para: juan.perez@example.com
Asunto: [FlightTracker] ¡Tu vuelo LA2040 despega en 24 horas!
Cuerpo:
Hola Juan,
Te recordamos que tu vuelo Lima (LIM) -> Cusco (CUZ) está programado
para el día 10/09/2026 a las 08:30 hrs.
Ya puedes realizar tu Web Check-in en la web de LATAM Airlines.
Estado actual del vuelo: A TIEMPO.
==========================================================================
```
