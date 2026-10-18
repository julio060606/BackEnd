# 📋 Bitácora de Correcciones Menores y Mejoras de UX/UI (Sprint 2 - Backend Core)

Este documento centraliza todas las observaciones, detalles de experiencia de usuario (UX/UI) y mejoras pendientes recolectadas durante el desarrollo de cada sprint. Se resolverán en una fase de afinamiento final para no bloquear el avance técnico de las historias de usuario de la rúbrica.

---

## 👤 Integrante 2: Módulo de Autenticación y Gestión de Usuarios (`US-S2-02`)

### 1. Estado Dinámico en Header tras Iniciar Sesión (UX/UI)
- **Problema detectado:** Al iniciar sesión exitosamente con `demo@chasquifly.com`, el Header de la aplicación sigue mostrando los botones de *"Iniciar sesión"* y *"Registrarse"*, en lugar de actualizarse dinámicamente.
- **Comportamiento esperado en la entrega final:**
  - Detectar el estado activo mediante `authService.isAuthenticated()` o escuchando los cambios en el almacenamiento local.
  - Ocultar los botones *"Iniciar sesión"* y *"Registrarse"*.
  - Mostrar el avatar con las iniciales o el nombre del usuario (`Pasajero Demostración`), con un menú desplegable que contenga:
    - 👤 **Mi Perfil:** Ver detalles personales (Nombre, Email, Teléfono, Rol, Fecha de creación).
    - ✈️ **Mis Viajes Guardados:** Enlace directo a `/trips`.
    - 🚪 **Cerrar Sesión:** Llamar a `authService.logout()` y redirigir al inicio.

### 2. Vista o Modal de Detalles de Usuario (`/profile` o `/account`)
- **Problema detectado:** No existe una vista visual dedicada donde el usuario pueda consultar o editar la información de su cuenta.
- **Comportamiento esperado en la entrega final:**
  - Crear una pantalla `/profile` protegida que muestre los datos retornados por el backend en `UserSummaryDto` (`id`, `firstName`, `lastName`, `email`, `phone`, `role`, `createdAt`).

### 3. Justificación de Valor: ¿Para qué le sirve al usuario iniciar sesión en ChasquiFly?
- **Persistencia de Itinerarios (`US-S2-06`):** Solo los usuarios con sesión activa pueden guardar vuelos en su lista personalizada de "Mis Viajes" (`POST /api/v1/trips`), sincronizados en la base de datos Supabase en la nube.
- **Alertas de Precio en Tiempo Real:** Configurar recordatorios para recibir notificaciones cuando una tarifa baje (`/api/v1/alerts`).
- **Control de Roles y Administración:**
  - Usuario Regular (`ROLE_USER`): Compara, guarda itinerarios y gestiona su perfil.
  - Administrador (`ROLE_ADMIN`, ej. `admin@chasquifly.com`): Permiso exclusivo para crear, actualizar y dar de baja vuelos en la plataforma (`POST /api/v1/flights`).

---

## 👤 Integrante 3: Persistencia Supabase, Migraciones Flyway y Catálogos (`US-S2-03`)
*(Se registrarán observaciones durante su ejecución)*

---

## 👤 Integrante 4: CRUD de Vuelos y Entidad Relacional (`US-S2-04`)
*(Se registrarán observaciones durante su ejecución)*

---

## 👤 Integrante 5: Motor de Búsqueda Multicriterio y Filtros (`US-S2-05`)
*(Se registrarán observaciones durante su ejecución)*

---

## 👤 Integrante 6: Comparador, Tablero FIDS e Itinerarios (`US-S2-06`)
*(Se registrarán observaciones durante su ejecución)*

---

## 👤 Integrante 7: Cliente Duffel API, Docker y Render (`US-S2-07`)
*(Se registrarán observaciones durante su ejecución)*
