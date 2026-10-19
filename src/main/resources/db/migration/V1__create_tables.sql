-- ==============================================================================
-- Flyway Migration: V1__create_tables.sql
-- Description: Initial schema for ChasquiFly (Single-Tenant, 3FN)
-- ==============================================================================

-- Enable pgcrypto extension for UUID generation if not present
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 1. Table: users
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(30) NOT NULL DEFAULT 'ROLE_USER',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- 2. Table: airports
-- Cataloga los terminales aéreos con código IATA estandarizado (3 letras)
CREATE TABLE IF NOT EXISTS airports (
    id SERIAL PRIMARY KEY,
    iata_code VARCHAR(3) NOT NULL UNIQUE,       -- Código único internacional IATA (ej. LIM, CUZ)
    name VARCHAR(150) NOT NULL,                 -- Nombre oficial del aeropuerto
    city VARCHAR(100) NOT NULL,                 -- Ciudad donde opera
    country VARCHAR(100) NOT NULL,              -- País de ubicación (Perú por defecto en catálogos iniciales)
    timezone VARCHAR(50) NOT NULL DEFAULT 'America/Lima' -- Zona horaria estándar para conversiones de itinerarios
);

-- 3. Table: airlines
-- Entidad de aerolíneas comerciales asociadas a los itinerarios de vuelo
CREATE TABLE IF NOT EXISTS airlines (
    id SERIAL PRIMARY KEY,
    iata_code VARCHAR(2) NOT NULL UNIQUE,       -- Código IATA de 2 caracteres (ej. LA, H2, JA)
    name VARCHAR(100) NOT NULL,                 -- Razón social o nombre comercial
    logo_url VARCHAR(255)                       -- Enlace o ruta al isotipo/logotipo
);

-- 4. Table: flights
CREATE TABLE IF NOT EXISTS flights (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    flight_number VARCHAR(20) NOT NULL,
    airline_id INT NOT NULL REFERENCES airlines(id) ON UPDATE CASCADE,
    origin_airport_id INT NOT NULL REFERENCES airports(id) ON UPDATE CASCADE,
    destination_airport_id INT NOT NULL REFERENCES airports(id) ON UPDATE CASCADE,
    departure_time TIMESTAMP WITH TIME ZONE NOT NULL,
    arrival_time TIMESTAMP WITH TIME ZONE NOT NULL,
    base_price NUMERIC(10,2) NOT NULL,
    available_seats INT NOT NULL DEFAULT 100,
    stops_count INT NOT NULL DEFAULT 0,
    duration_minutes INT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'SCHEDULED',
    gate VARCHAR(20),
    terminal VARCHAR(10)
);

-- 5. Table: user_trips
CREATE TABLE IF NOT EXISTS user_trips (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    flight_id UUID NOT NULL REFERENCES flights(id) ON UPDATE CASCADE,
    booking_reference VARCHAR(50),
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- 6. Table: notifications_queue
CREATE TABLE IF NOT EXISTS notifications_queue (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    trip_id UUID NOT NULL REFERENCES user_trips(id) ON DELETE CASCADE,
    notify_time TIMESTAMP WITH TIME ZONE NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    sent_at TIMESTAMP WITH TIME ZONE,
    message_payload TEXT NOT NULL
);

-- Optimization Indexes
CREATE INDEX IF NOT EXISTS idx_flights_search ON flights(origin_airport_id, destination_airport_id, departure_time, base_price);
CREATE INDEX IF NOT EXISTS idx_flights_status ON flights(flight_number, status);
CREATE INDEX IF NOT EXISTS idx_notifications_queue_pending ON notifications_queue(status, notify_time);
CREATE INDEX IF NOT EXISTS idx_user_trips_user ON user_trips(user_id);
