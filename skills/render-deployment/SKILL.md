---
name: render-deployment
description: Directivas y configuración para el despliegue del backend de ChasquiFly en Render.com.
---

# Skill: Render.com Cloud Deployment Guide

Esta skill detalla los pasos para desplegar el backend en el entorno gratuito de Render.com cumpliendo los requerimientos del profesor para el Avance de Proyecto Final 2 (APF2).

---

## 🚀 1. Tipo de Servicio en Render
- **Tipo:** Web Service.
- **Entorno (Environment):** Docker (utiliza el `Dockerfile` optimizado en la carpeta `BackEnd/`).
- **Health Check Path:** `/actuator/health`

---

## 🔑 2. Variables de Entorno en el Dashboard de Render
Configurar en la sección **Environment Variables**:

| Variable | Valor / Descripción |
| :--- | :--- |
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `SERVER_PORT` | `8080` |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://<SUPABASE_HOST>:5432/postgres?sslmode=require` |
| `SPRING_DATASOURCE_USERNAME` | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | `<CONTRASEÑA_SUPABASE>` |
| `JWT_SECRET` | Generar un hash hexadecimal seguro de 64 caracteres |
| `CORS_ALLOWED_ORIGINS` | `https://chasquifly.vercel.app,http://localhost:5173` |

---

## ⚡ 3. Blueprint Automático (`render.yaml`)
El repositorio incluye `BackEnd/render.yaml` para permitir aprovisionamiento como **Blueprint Instance** en Render con un solo clic.
