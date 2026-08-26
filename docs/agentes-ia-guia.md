# Guía de Contexto y Lectura Rápida para Agentes de IA

Este documento orienta a cualquier asistente o subagente de IA para acceder de forma inmediata al contexto correcto según la tarea asignada.

---

## 🎯 Directivas Globales Inmutables

1. **Fuente de Verdad:** Leer [`docs/00-contexto-maestro.md`](file:///d:/Projects/VS_code/HERRAMIENTAS%20DESARROLLO/indexador-venta-vuelos/docs/00-contexto-maestro.md).
2. **Single-Tenant Estricto:** Toda arquitectura de base de datos y backend es Single-Tenant. No incorporar `tenant_id` ni aislamiento multi-tenant.
3. **Docker Pausado:** No asumir Docker activo en desarrollo local. Dejar `Dockerfile` agnóstico como plantilla.
4. **Redis Condicional:** Toda lógica de caché en Spring Boot debe anotarse obligatoriamente con:
   ```java
   @ConditionalOnProperty(name = "app.cache.enabled", havingValue = "true")
   ```
   Si la propiedad es `false` (valor por defecto), el sistema consulta directamente a PostgreSQL.
5. **Calidad y Simplicidad:** Código Clean Code, SOLID, KISS y DRY, comprensible y mantenible por un equipo de 7 integrantes.
6. **Mantenimiento Documental:** Al concluir cualquier desarrollo o cambio de alcance, redactar el resumen de actualización para la bitácora correspondiente en `docs/07-sprints/`.

---

## 📚 Matriz de Consulta Rápida

| Tarea Asignada | Documentos Obligatorios a Consultar |
| :--- | :--- |
| **Diseño / Componentes UI** | [`docs/02-ui-ux/01-sistema-diseno-guia-estilos.md`](file:///d:/Projects/VS_code/HERRAMIENTAS%20DESARROLLO/indexador-venta-vuelos/docs/02-ui-ux/01-sistema-diseno-guia-estilos.md)<br>[`docs/07-sprints/01-sprint-1-frontend.md`](file:///d:/Projects/VS_code/HERRAMIENTAS%20DESARROLLO/indexador-venta-vuelos/docs/07-sprints/01-sprint-1-frontend.md) |
| **Formularios y Validaciones** | [`docs/02-ui-ux/02-estandares-formularios-zod.md`](file:///d:/Projects/VS_code/HERRAMIENTAS%20DESARROLLO/indexador-venta-vuelos/docs/02-ui-ux/02-estandares-formularios-zod.md) |
| **Lógica de un Módulo** | Carpeta [`docs/04-modulos/`](file:///d:/Projects/VS_code/HERRAMIENTAS%20DESARROLLO/indexador-venta-vuelos/docs/04-modulos) (revisar el archivo específico del módulo) |
| **API REST / DTOs / Mock Data** | [`docs/03-api/01-contratos-endpoints.md`](file:///d:/Projects/VS_code/HERRAMIENTAS%20DESARROLLO/indexador-venta-vuelos/docs/03-api/01-contratos-endpoints.md)<br>[`docs/03-api/02-manejo-errores-y-respuestas.md`](file:///d:/Projects/VS_code/HERRAMIENTAS%20DESARROLLO/indexador-venta-vuelos/docs/03-api/02-manejo-errores-y-respuestas.md) |
| **Modelo de BD / Migraciones** | [`docs/05-bd/01-diccionario-datos-postgresql.md`](file:///d:/Projects/VS_code/HERRAMIENTAS%20DESARROLLO/indexador-venta-vuelos/docs/05-bd/01-diccionario-datos-postgresql.md)<br>[`docs/05-bd/02-estrategia-migraciones-flyway.md`](file:///d:/Projects/VS_code/HERRAMIENTAS%20DESARROLLO/indexador-venta-vuelos/docs/05-bd/02-estrategia-migraciones-flyway.md) |
| **Seguridad / JWT / Roles** | [`docs/06-seguridad/01-politicas-seguridad-y-jwt.md`](file:///d:/Projects/VS_code/HERRAMIENTAS%20DESARROLLO/indexador-venta-vuelos/docs/06-seguridad/01-politicas-seguridad-y-jwt.md)<br>[`docs/06-seguridad/02-control-acceso-rbac.md`](file:///d:/Projects/VS_code/HERRAMIENTAS%20DESARROLLO/indexador-venta-vuelos/docs/06-seguridad/02-control-acceso-rbac.md) |
| **Planificación del Sprint** | Archivo correspondiente en [`docs/07-sprints/`](file:///d:/Projects/VS_code/HERRAMIENTAS%20DESARROLLO/indexador-venta-vuelos/docs/07-sprints) |
