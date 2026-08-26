# Políticas de Seguridad Globales y Manejo de JWT (Security by Design)

---

## 🔐 1. Flujo de Autenticación con JWT y Refresh Token

```
 [CLIENTE FRONTEND]                                      [BACKEND SPRING BOOT]
         │                                                         │
         │─── 1. POST /api/v1/auth/login {email, password} ───────►│ Valida credenciales con BCrypt
         │                                                         │ Genera AccessToken (15-30m)
         │◄── 2. 200 OK {accessToken, refreshToken, user} ─────────│ y RefreshToken (7d)
         │                                                         │
         │ (Almacena accessToken en memoria / Zustand/Context)     │
         │ (Almacena refreshToken en LocalStorage / Cookie segura) │
         │                                                         │
         │─── 3. Petición protegida: Header 'Bearer <token>' ─────►│ Filtro JwtAuthFilter valida firma
         │◄── 4. 200 OK Datos solicitados ─────────────────────────│ y autoriza acceso al recurso
         │                                                         │
    [Token expira]                                                 │
         │─── 5. Petición protegida (401 Unauthorized) ───────────►│
         │─── 6. POST /api/v1/auth/refresh {refreshToken} ────────►│ Valida RefreshToken
         │◄── 7. 200 OK {newAccessToken, newRefreshToken} ────────│ y rota ambos tokens
```

---

## 🛡️ 2. Medidas de Mitigación de Vulnerabilidades

1. **Inyección SQL:**
   - Mitigada al 100% mediante el uso de JPA y consultas preparadas (`PreparedStatement`).
   - Se prohíbe explícitamente el uso de `Statement` con concatenación manual de cadenas.
2. **Cross-Site Scripting (XSS):**
   - React escapa de forma nativa variables en JSX.
   - Cabeceras de seguridad HTTP activas en Spring Security (`Content-Security-Policy`, `X-Content-Type-Options: nosniff`).
3. **Cross-Site Request Forgery (CSRF):**
   - Arquitectura stateless con tokens JWT transmitidos en el encabezado `Authorization: Bearer <token>`.
4. **Fuerza Bruta y DoS:**
   - Rate limiting en endpoints sensibles (`/api/v1/auth/login`, `/api/v1/auth/register`).
5. **Cifrado de Contraseñas:**
   - Algoritmo `BCryptPasswordEncoder` con factor de trabajo (work factor) de 10.
