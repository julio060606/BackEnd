# Control de Acceso Basado en Roles (RBAC)

Define la matriz de permisos y la protección de rutas tanto en Frontend como en Backend.

---

## 👥 Roles Definidos

1. **`ROLE_ANONYMOUS` (Público):** Visitantes no autenticados.
2. **`ROLE_USER` (Usuario Estándar):** Pasajeros registrados.
3. **`ROLE_ADMIN` (Administrador del Sistema):** Gestión de vuelos, catálogos e infraestructura.

---

## 🔐 Matriz de Permisos por Endpoint

| Endpoint | Método | `ANONYMOUS` | `ROLE_USER` | `ROLE_ADMIN` |
| :--- | :--- | :---: | :---: | :---: |
| `/api/v1/auth/register` | `POST` | ✅ | ✅ | ✅ |
| `/api/v1/auth/login` | `POST` | ✅ | ✅ | ✅ |
| `/api/v1/airports` | `GET` | ✅ | ✅ | ✅ |
| `/api/v1/flights/search` | `GET` | ✅ | ✅ | ✅ |
| `/api/v1/flight-status/live` | `GET` | ✅ | ✅ | ✅ |
| `/api/v1/users/profile` | `GET`, `PUT` | ❌ | ✅ | ✅ |
| `/api/v1/trips/**` | `GET`, `POST`, `DELETE` | ❌ | ✅ | ✅ |
| `/api/v1/alerts/**` | `POST`, `PUT` | ❌ | ✅ | ✅ |
| `/api/v1/admin/flights/**` | `POST`, `PUT`, `DELETE` | ❌ | ❌ | ✅ |

---

## 🛡️ Implementación en Frontend (ProtectedRoute)
El cliente React cuenta con un componente de envoltura `<ProtectedRoute requiredRole="ROLE_USER">` que redirige a `/login` si no hay sesión activa, o a `/403` si el rol es insuficiente.
