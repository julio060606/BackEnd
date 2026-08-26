# Contratos de Endpoints (API REST Global)

Esta especificación cubre todos los endpoints del sistema que el Backend en Spring Boot expondrá y el Frontend consumirá.

---

## 1. Módulo de Autenticación (`/api/v1/auth`)

### `POST /api/v1/auth/register`
- **Público:** Sí
- **Request Body:**
  ```json
  {
    "firstName": "Juan",
    "lastName": "Pérez",
    "email": "juan.perez@example.com",
    "password": "Password123",
    "phone": "+51987654321"
  }
  ```
- **Response `201 Created`:**
  ```json
  {
    "userId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "message": "Usuario registrado exitosamente"
  }
  ```

### `POST /api/v1/auth/login`
- **Público:** Sí
- **Request Body:**
  ```json
  {
    "email": "juan.perez@example.com",
    "password": "Password123"
  }
  ```
- **Response `200 OK`:**
  ```json
  {
    "accessToken": "eyJhbGciOiJIUzI1NiIsIn...",
    "refreshToken": "dGhpcy1pcy1hLXJlZnJl...",
    "tokenType": "Bearer",
    "expiresIn": 1800,
    "user": {
      "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "email": "juan.perez@example.com",
      "firstName": "Juan",
      "lastName": "Pérez",
      "role": "ROLE_USER"
    }
  }
  ```

### `POST /api/v1/auth/refresh`
- **Público:** Sí
- **Request Body:** `{ "refreshToken": "..." }`
- **Response `200 OK`:** `{ "accessToken": "...", "refreshToken": "..." }`

---

## 2. Módulo de Catálogos y Búsqueda de Vuelos (`/api/v1/flights`, `/api/v1/airports`)

### `GET /api/v1/airports`
- **Público:** Sí
- **Response `200 OK`:**
  ```json
  [
    { "id": 1, "iataCode": "LIM", "name": "Aeropuerto Internacional Jorge Chávez", "city": "Lima", "country": "Perú" },
    { "id": 2, "iataCode": "CUZ", "name": "Aeropuerto Alejandro Velasco Astete", "city": "Cusco", "country": "Perú" }
  ]
  ```

### `GET /api/v1/flights/search`
- **Público:** Sí
- **Query Params:** `origin=LIM&destination=CUZ&departureDate=2026-09-10&passengers=1&travelClass=ECONOMY&maxPrice=150&airlineId=1&maxStops=0`
- **Response `200 OK`:**
  ```json
  [
    {
      "flightId": "a1b2c3d4-0000-0000-0000-000000000001",
      "flightNumber": "LA2040",
      "airline": { "id": 1, "name": "LATAM Airlines", "iataCode": "LA", "logoUrl": "/logos/latam.png" },
      "origin": { "iataCode": "LIM", "city": "Lima" },
      "destination": { "iataCode": "CUZ", "city": "Cusco" },
      "departureTime": "2026-09-10T08:30:00Z",
      "arrivalTime": "2026-09-10T09:55:00Z",
      "durationMinutes": 85,
      "stopsCount": 0,
      "basePrice": 64.99,
      "currency": "USD",
      "availableSeats": 12,
      "status": "SCHEDULED"
    }
  ]
  ```

---

## 3. Módulo de Estado de Vuelos en Vivo (`/api/v1/flight-status`)

### `GET /api/v1/flight-status/live`
- **Público:** Sí
- **Query Params:** `airport=LIM&type=DEPARTURES`
- **Response `200 OK`:**
  ```json
  [
    {
      "flightNumber": "LA2040",
      "airline": "LATAM Airlines",
      "destination": "Cusco (CUZ)",
      "scheduledTime": "2026-09-10T08:30:00Z",
      "estimatedTime": "2026-09-10T08:30:00Z",
      "status": "BOARDING",
      "gate": "Gate 14",
      "terminal": "T1"
    }
  ]
  ```

---

## 4. Módulo de Viajes Guardados y Alertas (`/api/v1/trips`, `/api/v1/alerts`)

### `GET /api/v1/trips`
- **Requiere Auth:** Sí (`ROLE_USER`)
- **Response `200 OK`:** Lista de viajes asociados al usuario autenticado.

### `POST /api/v1/trips`
- **Requiere Auth:** Sí
- **Request Body:** `{ "flightId": "uuid", "notes": "Viaje de vacaciones" }`
- **Response `201 Created`:** `{ "tripId": "uuid", "createdAt": "..." }`

### `POST /api/v1/alerts/configure`
- **Requiere Auth:** Sí
- **Request Body:**
  ```json
  {
    "tripId": "uuid",
    "notify24h": true,
    "notify3h": true,
    "notifyStatusChanges": true,
    "email": "juan.perez@example.com"
  }
  ```
- **Response `200 OK`:** `{ "message": "Alertas configuradas con éxito", "activeAlerts": 3 }`
