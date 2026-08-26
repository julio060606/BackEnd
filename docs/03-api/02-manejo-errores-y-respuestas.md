# Formato Estándar de Errores y Respuestas HTTP (RFC 7807)

Para garantizar consistencia entre el Backend en Spring Boot y el Frontend en React, todas las respuestas de error utilizarán una estructura estandarizada de tipo **Problem Details** (RFC 7807).

---

## 🛑 Estructura Estándar de Error JSON

```json
{
  "timestamp": "2026-09-10T14:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Los parámetros de búsqueda son inválidos",
  "path": "/api/v1/flights/search",
  "validationErrors": [
    {
      "field": "returnDate",
      "rejectedValue": "2026-09-01",
      "message": "La fecha de regreso no puede ser anterior a la de salida"
    }
  ]
}
```

---

## 📊 Códigos de Estado HTTP Utilizados

| Código HTTP | Significado | Caso de Uso |
| :--- | :--- | :--- |
| `200 OK` | Éxito | Consultas exitosas (`GET`, `PUT`). |
| `201 Created` | Recurso Creado | Registro de usuario, nuevo viaje guardado, alerta creada (`POST`). |
| `204 No Content` | Sin Contenido | Eliminación exitosa de un viaje (`DELETE`). |
| `400 Bad Request` | Petición Inválida | Fallos en validaciones de entrada (`@Valid` o reglas de negocio). |
| `401 Unauthorized` | No Autenticado | Token JWT ausente, expirado o firma inválida. |
| `403 Forbidden` | Acceso Denegado | Rol insuficiente para el recurso (ej. usuario intentando acción admin). |
| `404 Not Found` | No Encontrado | Vuelo, usuario o viaje no existente por ID. |
| `409 Conflict` | Conflicto | Intento de registrar un correo electrónico ya existente. |
| `429 Too Many Requests` | Rate Limit Excedido | Exceso de peticiones en endpoints de login o búsqueda intensiva. |
| `500 Internal Server Error`| Error del Servidor | Error no controlado (capturado por `@RestControllerAdvice`). |

---

## 🛡️ Interceptor de Axios en Frontend
El cliente frontend captura automáticamente las respuestas `401 Unauthorized` para intentar el refresco del token con `/api/v1/auth/refresh` y, en caso de fallo, redirigir limpiamente a la pantalla de `/login`.
