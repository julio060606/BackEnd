# Diccionario de Datos y Esquema Relacional (PostgreSQL)

> **Principio de Arquitectura:** Enfoque **Single-Tenant** estricto normalizado en Tercera Forma Normal (3FN) con índices optimizados para lecturas concurrentes.

---

## 📊 Diagrama Entidad-Relación y Tablas

```
 ┌──────────────┐         ┌───────────────┐
 │   airports   │◄────┬───┤    flights    ├───►┌───────────────┐
 └──────────────┘     │   └───────┬───────┘    │   airlines    │
                      │           │            └───────────────┘
                      │           ▼
 ┌──────────────┐     │   ┌───────────────┐
 │    users     │◄────┼───┤  user_trips   │
 └──────┬───────┘     │   └───────┬───────┘
        │             │           │
        ▼             │           ▼
 ┌──────────────┐     │   ┌─────────────────────┐
 │  user_roles  │     └───┤ notifications_queue │
 └──────────────┘         └─────────────────────┘
```

---

## 📋 Detalle de Tablas y Columnas

### 1. `users` (Usuarios del Sistema)
| Columna | Tipo | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | UUID | PK, DEFAULT gen_random_uuid() | Identificador global único del usuario |
| `email` | VARCHAR(150) | UNIQUE, NOT NULL | Correo electrónico principal |
| `password_hash` | VARCHAR(255) | NOT NULL | Contraseña encriptada con BCrypt |
| `first_name` | VARCHAR(100) | NOT NULL | Nombres |
| `last_name` | VARCHAR(100) | NOT NULL | Apellidos |
| `phone` | VARCHAR(20) | NULL | Teléfono móvil para alertas |
| `role` | VARCHAR(30) | NOT NULL, DEFAULT 'ROLE_USER' | Rol de autorización (`ROLE_USER`, `ROLE_ADMIN`) |
| `is_active` | BOOLEAN | NOT NULL, DEFAULT TRUE | Indicador de cuenta activa |
| `created_at` | TIMESTAMP WITH TIME ZONE | NOT NULL, DEFAULT NOW() | Fecha de registro |
| `updated_at` | TIMESTAMP WITH TIME ZONE | NOT NULL, DEFAULT NOW() | Última modificación |

### 2. `airports` (Catálogo de Aeropuertos)
| Columna | Tipo | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | SERIAL | PK | ID autoincremental |
| `iata_code` | VARCHAR(3) | UNIQUE, NOT NULL | Código IATA de 3 letras (ej. LIM, CUZ, JFK) |
| `name` | VARCHAR(150) | NOT NULL | Nombre del aeropuerto |
| `city` | VARCHAR(100) | NOT NULL | Ciudad donde se ubica |
| `country` | VARCHAR(100) | NOT NULL | País |
| `timezone` | VARCHAR(50) | NOT NULL | Zona horaria (ej. 'America/Lima') |

### 3. `airlines` (Aerolíneas)
| Columna | Tipo | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | SERIAL | PK | ID autoincremental |
| `iata_code` | VARCHAR(2) | UNIQUE, NOT NULL | Código IATA de 2 letras (ej. LA, AV, IB) |
| `name` | VARCHAR(100) | NOT NULL | Nombre comercial de la aerolínea |
| `logo_url` | VARCHAR(255) | NULL | URL del icono o logotipo |

### 4. `flights` (Vuelos e Itinerarios)
| Columna | Tipo | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | UUID | PK, DEFAULT gen_random_uuid() | ID único del vuelo |
| `flight_number` | VARCHAR(20) | NOT NULL | Código comercial de vuelo (ej. LA2040) |
| `airline_id` | INT | NOT NULL, FK -> `airlines(id)` | Aerolínea operadora |
| `origin_airport_id` | INT | NOT NULL, FK -> `airports(id)` | Aeropuerto de origen |
| `destination_airport_id` | INT | NOT NULL, FK -> `airports(id)` | Aeropuerto de destino |
| `departure_time` | TIMESTAMP WITH TIME ZONE | NOT NULL | Fecha y hora programada de salida |
| `arrival_time` | TIMESTAMP WITH TIME ZONE | NOT NULL | Fecha y hora programada de llegada |
| `base_price` | NUMERIC(10,2) | NOT NULL | Tarifa base por pasajero en USD |
| `available_seats` | INT | NOT NULL | Asientos disponibles |
| `stops_count` | INT | NOT NULL, DEFAULT 0 | Número de escalas (0 = Directo) |
| `duration_minutes` | INT | NOT NULL | Duración total en minutos |
| `status` | VARCHAR(30) | NOT NULL, DEFAULT 'SCHEDULED' | `SCHEDULED`, `BOARDING`, `DELAYED`, `CANCELLED`, `LANDED` |
| `gate` | VARCHAR(20) | NULL | Puerta de embarque asignada |
| `terminal` | VARCHAR(10) | NULL | Terminal aeroportuaria |

### 5. `user_trips` (Viajes Seguidos / Guardados por Usuarios)
| Columna | Tipo | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | UUID | PK, DEFAULT gen_random_uuid() | ID único del seguimiento |
| `user_id` | UUID | NOT NULL, FK -> `users(id)` ON DELETE CASCADE | Usuario dueño del viaje |
| `flight_id` | UUID | NOT NULL, FK -> `flights(id)` | Vuelo indexado |
| `booking_reference` | VARCHAR(50) | NULL | Código de reserva PNR (opcional) |
| `notes` | TEXT | NULL | Notas personalizadas |
| `created_at` | TIMESTAMP WITH TIME ZONE | NOT NULL, DEFAULT NOW() | Fecha de registro |

### 6. `notifications_queue` (Cola de Alertas y Proximidad)
| Columna | Tipo | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | UUID | PK, DEFAULT gen_random_uuid() | ID de la notificación |
| `user_id` | UUID | NOT NULL, FK -> `users(id)` ON DELETE CASCADE | Destinatario |
| `trip_id` | UUID | NOT NULL, FK -> `user_trips(id)` ON DELETE CASCADE | Viaje asociado |
| `notify_time` | TIMESTAMP WITH TIME ZONE | NOT NULL | Momento programado de envío |
| `type` | VARCHAR(50) | NOT NULL | `PROXIMITY_24H`, `PROXIMITY_3H`, `STATUS_CHANGE` |
| `status` | VARCHAR(30) | NOT NULL, DEFAULT 'PENDING' | `PENDING`, `SENT`, `FAILED` |
| `sent_at` | TIMESTAMP WITH TIME ZONE | NULL | Momento efectivo de disparo |
| `message_payload` | TEXT | NOT NULL | Contenido del correo en texto/HTML |

---

## ⚡ Índices de Optimización
```sql
CREATE INDEX idx_flights_search ON flights(origin_airport_id, destination_airport_id, departure_time, base_price);
CREATE INDEX idx_flights_status ON flights(flight_number, status);
CREATE INDEX idx_notifications_queue_pending ON notifications_queue(status, notify_time);
CREATE INDEX idx_user_trips_user ON user_trips(user_id);
```
