# Módulo 01: Autenticación y Gestión de Usuarios

Este módulo gestiona la identidad, el registro, la seguridad y las sesiones de los usuarios en la plataforma.

---

## 🎯 Alcance Funcional
1. **Registro de Usuarios:** Creación de cuenta con validación de unicidad de email, contraseña robusta (BCrypt) y datos personales.
2. **Inicio de Sesión (Login):** Autenticación mediante credenciales con emisión de par de tokens: `accessToken` (corta duración) y `refreshToken` (larga duración).
3. **Refresco de Sesión:** Renovación transparente de tokens sin obligar al usuario a reingresar credenciales.
4. **Gestión de Perfil:** Consulta y actualización de datos de contacto (nombre, teléfono).
5. **Cierre de Sesión:** Invalidación del refresh token y limpieza de almacenamiento en cliente.

---

## 🔒 Reglas de Negocio
- El correo electrónico es único en todo el sistema y no puede duplicarse.
- Las contraseñas deben contener mínimo 8 caracteres, al menos una mayúscula y un número.
- Rol por defecto asignado: `ROLE_USER`.
- Cuentas bloqueadas (`is_active = false`) no pueden iniciar sesión.
