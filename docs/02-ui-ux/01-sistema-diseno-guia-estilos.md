# Sistema de Diseño y Guía de Estilos UI/UX

Este documento define las directrices visuales y de experiencia de usuario para toda la plataforma **FlightTracker & Price Comparator** utilizando **Material UI (MUI v5+)** y React.

---

## 🎨 Paleta de Colores Aeronáutica

| Uso / Variable | Código Hexadecimal | Significado / Contexto |
| :--- | :--- | :--- |
| **Primary Main** | `#0A4D68` | Azul Aeronáutico Profundo (Header, botones principales, énfasis) |
| **Primary Light** | `#088395` | Azul Océano (Hover de botones, tabs activos) |
| **Secondary Main** | `#05BFDB` | Cian Brillante (Badges, acentos de selección, alertas informativas) |
| **Background Default** | `#F8FAFC` | Fondo general de la aplicación (Gris pizarra ultra claro) |
| **Background Paper** | `#FFFFFF` | Fondo de tarjetas (Cards), modales y contenedores |
| **Success (A tiempo / Confirmado)** | `#10B981` | Verde Esmeralda (Vuelos a tiempo, reservas confirmadas) |
| **Warning (Demorado / Advertencia)** | `#F59E0B` | Ámbar (Vuelos demorados, recordatorio de check-in) |
| **Error (Cancelado / Fallo)** | `#EF4444` | Rojo Carmesí (Vuelos cancelados, errores de validación) |
| **Info (Embarcando / Información)** | `#3B82F6` | Azul Cielo (Vuelos en puerta de embarque) |

---

## 🔤 Tipografía y Jerarquía Visual

- **Fuente:** `Inter`, `Roboto`, `Helvetica Neue`, sans-serif.
- **`h1` (Hero / Títulos de página principales):** 2.25rem (36px), font-weight 700.
- **`h2` (Títulos de sección):** 1.75rem (28px), font-weight 600.
- **`h3` (Títulos de tarjetas y modales):** 1.25rem (20px), font-weight 600.
- **`body1` (Texto principal):** 1rem (16px), font-weight 400.
- **`body2` (Texto secundario, itinerarios):** 0.875rem (14px), font-weight 400.
- **`caption` (Códigos de vuelo, etiquetas de escalas):** 0.75rem (12px), font-weight 500.

---

## 🧩 Componentes Base Reutilizables

1. **`FlightCard`:** Tarjeta de presentación de vuelo con aerolínea, horario salida/llegada, escalas, duración total, precio y botón de seguimiento/compra.
2. **`StatusChip`:** Chip con color y texto dinámico según el estado del vuelo (`A tiempo`, `Demorado (+45m)`, `Cancelado`, `Embarcando`).
3. **`SearchFilterSidebar`:** Panel lateral colapsable para filtrar por rango de precios (Slider), aerolíneas (Checkboxes), escalas (RadioGroup) y horarios.
4. **`LiveFlightTable`:** Tabla de salidas y llegadas tipo aeropuerto con buscador en vivo por código IATA o número de vuelo.
5. **`AlertModal`:** Ventana emergente para configurar recordatorios de proximidad (24h antes, 3h antes) y notificaciones de cambio de estado.

---

## 📱 Responsividad y Breakpoints
- **Mobile (`xs: 0px - 599px`):** Menú hamburguesa Drawer, tarjetas de vuelo en formato columna única apilada, filtros colapsados en drawer inferior.
- **Tablet (`sm / md: 600px - 1199px`):** Formulario de búsqueda en dos filas, grid de 2 columnas para dashboard.
- **Desktop (`lg / xl: 1200px+`):** Layout completo con sidebar de filtros permanente y listado de resultados centralizado con ancho máximo de 1280px.
