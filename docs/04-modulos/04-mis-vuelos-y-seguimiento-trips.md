# Módulo 04: Mis Viajes y Seguimiento de Vuelos (Trips)

Permite a los usuarios registrados gestionar su agenda de viajes, seguir vuelos de familiares o guardar ofertas de interés para monitoreo continuo.

---

## 🎯 Alcance Funcional
1. **Guardado de Vuelos (Follow / Track):**
   - El usuario agrega cualquier vuelo indexado a su panel personal con un clic.
   - Posibilidad de ingresar notas personales (ej. "Viaje de trabajo", "Vuelo de mamá").
2. **Panel de Control de Mis Viajes:**
   - Lista organizada por viajes "Próximos" e "Históricos".
   - Conteo regresivo en vivo hacia la hora del despegue (`Faltan 2 días y 4 horas`).
3. **Gestión y Desvinculación:**
   - Opción para eliminar el seguimiento de un vuelo cuando ya no sea necesario.
   - Acceso directo a la configuración de alertas de proximidad para cada viaje guardado.

---

## ⚡ Reglas de Negocio
- Solo usuarios con sesión activa (`ROLE_USER`) pueden crear y ver sus viajes.
- No se pueden duplicar seguimientos activos sobre el mismo vuelo para el mismo usuario.
- Al guardar un viaje, se activan por defecto las alertas de proximidad estándar (24 horas antes y 3 horas antes).
