# Sprint 1: Diseño Frontend y Bases de Control de Versiones

- **Duración:** Semanas 1 a 6.
- **Hito de Evaluación:** **Avance de Proyecto Final 1 (APF1)** (Semana 6).
- **Objetivo Principal:** Construcción de la interfaz de usuario completa (UI/UX) con React, TypeScript, Vite y Material UI, dividida por secciones visuales bajo la regla estricta: **Todo componente debe contar con un diseño previo en Figma antes de programarse**.
- **Alcance Técnico:** Frontend exclusivo con Mock Data (Single-Tenant, sin lógica backend en este sprint).

---

## 🔄 Flujo de Trabajo Oficial del Sprint 1

```
 ┌────────────────┐       ┌──────────────────────┐       ┌────────────────────────┐       ┌─────────────────────┐
 │ 1. DISEÑO EN   │  ──►  │ 2. REVISIÓN DE       │  ──►  │ 3. IMPLEMENTACIÓN EN   │  ──►  │ 4. INTEGRACIÓN Y    │
 │    FIGMA       │       │    ARQUITECTURA/MUI  │       │    REACT + TS + MUI    │       │    CONTROL EN HUBS  │
 └────────────────┘       └──────────────────────┘       └────────────────────────┘       └─────────────────────┘
```

1. **Figma First (Diseño previo mandatorio):** Ningún desarrollador inicia código en React sin el prototipo/frame en Figma validado para su sección.
2. **Implementación Modular por Secciones:** Cada integrante desarrolla su componente en `src/components/` o `src/modules/` garantizando coincidencia con los tokens de diseño (colores, espaciados y tipografía).
3. **Puntos de Control (3 Reuniones de Hito en el Sprint):**
   - **Reunión de Control 1 (Semana 2):** Cierre y aprobación de Wireframes/Mockups en Figma + Scaffolding de Vite, Routing y Theme MUI.
   - **Reunión de Control 2 (Semana 4):** Revisión de componentes atómicos/moleculares construidos y ensamblaje de pantallas principales (Buscador, Resultados, Board).
   - **Reunión de Control 3 (Semana 6):** QA visual, responsividad en móviles/tablets/desktop, integración con Mock Data y entrega de **APF1**.

---

## 🏛️ Asignación Arquitectónica de Secciones y Componentes MUI

| Sección Visual | Componentes de Material UI (MUI v5) | Responsabilidad / Descripción | Asignación (Equipo de 7) |
| :--- | :--- | :--- | :--- |
| **1. Header & Navegación** | `AppBar`, `Toolbar`, `IconButton`, `Menu`, `MenuItem`, `Drawer` (mobile), `Badge`, `Avatar` | Barra superior responsiva con logo, navegación a módulos, dropdown de perfil/notificaciones y menú hamburguesa para móviles. | **Integrante 2** |
| **2. Hero Section** | `Box`, `Container`, `Typography`, `Stack` con gradiente `linear-gradient` aeronáutico | Encabezado principal con título de impacto, subtítulo persuasivo y fondo temático aeroportuario. | **Integrante 1 (Scrum Master)** |
| **3. Barra / Formulario de Búsqueda** | `Paper`, `Grid`, `Autocomplete`, `TextField`, `Select`, `Button`, `ToggleButtonGroup`, `Popper` | Selector de viaje (Solo ida / Ida y vuelta), inputs de Origen/Destino con códigos IATA, selector de fechas, pasajeros y clase. Validado con **Zod**. | **Integrante 4** |
| **4. Panel Lateral de Filtros** | `Paper` (desktop) / `Drawer` (mobile), `Slider`, `FormGroup`, `FormControlLabel`, `Checkbox`, `RadioGroup`, `Divider`, `Typography` | Filtros dinámicos por rango de precio (Slider doble), aerolíneas disponibles, número de escalas (Directo / 1 escala) y rangos horarios. | **Integrante 5** |
| **5. Tarjetas de Vuelo y Resultados** | `Card`, `CardContent`, `CardActions`, `Grid`, `Stack`, `Chip` (`StatusChip`), `Accordion`, `AccordionSummary`, `Button`, `Skeleton` (loading) | Listado comparativo de vuelos con logo de aerolínea, horas, escalas, duración total, desglose de precio y botón de compra/seguimiento. | **Integrante 5** |
| **6. Tablero en Vivo de Estado (FIDS)** | `TableContainer`, `Table`, `TableHead`, `TableBody`, `TableRow`, `TableCell`, `Tabs`, `Tab`, `TextField` (search rápido), `Chip` | Pantalla estilo aeropuerto con estatus en tiempo real (`A tiempo`, `Demorado`, `Cancelado`, `Embarcando`), puerta y terminal. | **Integrante 6** |
| **7. Mis Vuelos (Trips) y Modal de Alertas** | `Dialog`, `DialogTitle`, `DialogContent`, `DialogActions`, `Switch`, `FormControlLabel`, `List`, `ListItem`, `ListItemText`, `CountdownDisplay` | Vista de agenda de viajes guardados con contador regresivo de salida y modal para configurar alertas de proximidad (24h / 3h). | **Integrante 7** |
| **8. Autenticación (Login / Registro / Perfil)** | `Dialog` / `Card`, `TextField`, `Button`, `InputAdornment`, `IconButton` (ver password), `Alert`, `Link` | Modales y vistas de inicio de sesión, registro con validaciones inline de Zod y ficha de perfil. | **Integrante 3** |
| **9. Footer** | `Box`, `Container`, `Grid`, `Typography`, `Link`, `Divider` | Pie de página con enlaces corporativos, políticas de privacidad, redes y copyright. | **Integrante 2** |

---

## 📋 Criterios de Aceptación para el Hito APF1
1. **Figma Completo:** Prototipo interactivo navegable con todas las secciones y variantes responsive (Desktop 1440px y Mobile 375px).
2. **Fidelidad de Implementación:** El código React en `FrontEnd/` debe reflejar fielmente los estilos, espaciados (grid de 8px) y colores del Figma.
3. **Manejo de Estado UI con Mock Data:** Búsqueda y filtros funcionando con catálogo local tipado en TypeScript.
4. **Validaciones en Tiempo Real:** Todos los formularios integrados con React Hook Form y Zod mostrando feedback visual inmediato.
5. **Cero Lógica Backend:** No se requiere conexión a base de datos ni servidor Spring Boot en este sprint.
