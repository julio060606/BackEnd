# Módulo 02: Búsqueda y Comparación de Precios de Vuelos

Módulo central para la indexación, filtrado y comparación dinámica de itinerarios y tarifas de diversas aerolíneas.

---

## 🎯 Alcance Funcional
1. **Motor de Búsqueda de Vuelos:**
   - Modalidades: Solo ida (`ONE_WAY`) e Ida y vuelta (`ROUND_TRIP`).
   - Selección por autocompletado de aeropuertos de origen y destino con código IATA y ciudad.
   - Selección de fechas de viaje y número de pasajeros.
2. **Comparador de Precios y Opciones:**
   - Visualización de tarjetas de vuelo con precio total, desglose por pasajero, aerolínea, hora de salida, hora de llegada y duración estimada.
   - Detalle de escalas (tiempo de conexión y aeropuerto intermediario).
3. **Filtros Interactivos:**
   - Por rango de precios (mínimo - máximo).
   - Por aerolínea (LATAM, Avianca, Iberia, SKY, etc.).
   - Por cantidad de escalas (Directos, 1 Escala, 2+ Escalas).
   - Por horario de salida (Mañana, Tarde, Noche).
4. **Criterios de Ordenamiento:**
   - Más económico primero.
   - Menor duración de vuelo.
   - Salida más temprana / más tardía.

---

## ⚡ Reglas de Negocio
- La fecha de salida no puede ser una fecha pasada.
- En viajes de ida y vuelta, la fecha de retorno debe ser igual o posterior a la de salida.
- El origen y el destino no pueden ser el mismo aeropuerto.
- Si la propiedad de caché Redis está habilitada (`app.cache.enabled = true`), las consultas con parámetros idénticos se cachean con un TTL de 10 minutos.
