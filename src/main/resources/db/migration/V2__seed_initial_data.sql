-- ==============================================================================
-- Flyway Migration: V2__seed_initial_data.sql
-- Description: Seed initial airports, airlines, flights and demo users
-- ==============================================================================

-- 1. Seed demo users (password is 'Password123!' -> BCrypt: $2a$10$3zP5mO2kK9kH4z8z.7sUieV5z.K1G0fVzH9g5jM1l5g3e3b3c3d3e)
-- For reliable dev testing, BCrypt for 'Password123' is $2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.
INSERT INTO users (id, email, password_hash, first_name, last_name, phone, role, is_active)
VALUES 
    ('a0000000-0000-0000-0000-000000000001', 'admin@chasquifly.com', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'Admin', 'ChasquiFly', '+51999888777', 'ROLE_ADMIN', TRUE),
    ('a0000000-0000-0000-0000-000000000002', 'demo@chasquifly.com', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'Pasajero', 'Demostración', '+51912345678', 'ROLE_USER', TRUE)
ON CONFLICT (email) DO NOTHING;

-- 2. Seed Peruvian Airports (Catálogo inicial de principales aeropuertos comerciales del Perú)
-- Incluye código IATA estandarizado, ciudad sede y huso horario oficial de Perú (America/Lima)
INSERT INTO airports (id, iata_code, name, city, country, timezone)
VALUES 
    (1, 'LIM', 'Aeropuerto Internacional Jorge Chávez', 'Lima', 'Perú', 'America/Lima'),
    (2, 'CUZ', 'Aeropuerto Internacional Alejandro Velasco Astete', 'Cusco', 'Perú', 'America/Lima'),
    (3, 'AQP', 'Aeropuerto Internacional Rodríguez Ballón', 'Arequipa', 'Perú', 'America/Lima'),
    (4, 'TRU', 'Aeropuerto Internacional Capitán FAP Carlos Martínez de Pinillos', 'Trujillo', 'Perú', 'America/Lima'),
    (5, 'IQT', 'Aeropuerto Internacional Coronel FAP Francisco Secada Vignetta', 'Iquitos', 'Perú', 'America/Lima'),
    (6, 'PIU', 'Aeropuerto Internacional Capitán FAP Guillermo Concha Iberico', 'Piura', 'Perú', 'America/Lima'),
    (7, 'TBP', 'Aeropuerto Capitán FAP Pedro Canga Rodríguez', 'Tumbes', 'Perú', 'America/Lima')
ON CONFLICT (iata_code) DO NOTHING;

-- Sincronización de secuencia para evitar colisiones de IDs autoincrementables en inserciones manuales
SELECT setval('airports_id_seq', (SELECT MAX(id) FROM airports));

-- 3. Seed Airlines (Operadores aéreos principales con presencia nacional e internacional)
INSERT INTO airlines (id, iata_code, name, logo_url)
VALUES 
    (1, 'LA', 'LATAM Airlines', '/logos/latam.png'),
    (2, 'H2', 'Sky Airline', '/logos/sky.png'),
    (3, 'JA', 'JetSMART Perú', '/logos/jetsmart.png')
ON CONFLICT (iata_code) DO NOTHING;

-- Sincronización de secuencia de llaves primarias para la tabla airlines
SELECT setval('airlines_id_seq', (SELECT MAX(id) FROM airlines));

-- 4. Seed Representative Flights (Domestic routes within Peru)
INSERT INTO flights (id, flight_number, airline_id, origin_airport_id, destination_airport_id, departure_time, arrival_time, base_price, available_seats, stops_count, duration_minutes, status, gate, terminal)
VALUES 
    ('f0000001-0000-0000-0000-000000000001', 'LA2040', 1, 1, 2, NOW() + INTERVAL '1 day' + INTERVAL '8 hour', NOW() + INTERVAL '1 day' + INTERVAL '9 hour 25 minute', 64.99, 45, 0, 85, 'SCHEDULED', '14', 'T1'),
    ('f0000001-0000-0000-0000-000000000002', 'H25102', 2, 1, 2, NOW() + INTERVAL '1 day' + INTERVAL '10 hour', NOW() + INTERVAL '1 day' + INTERVAL '11 hour 20 minute', 49.50, 20, 0, 80, 'SCHEDULED', '08', 'T1'),
    ('f0000001-0000-0000-0000-000000000003', 'JA3010', 3, 1, 2, NOW() + INTERVAL '1 day' + INTERVAL '14 hour', NOW() + INTERVAL '1 day' + INTERVAL '15 hour 30 minute', 39.00, 12, 0, 90, 'SCHEDULED', '03', 'T1'),
    ('f0000001-0000-0000-0000-000000000004', 'LA2110', 1, 1, 3, NOW() + INTERVAL '1 day' + INTERVAL '9 hour', NOW() + INTERVAL '1 day' + INTERVAL '10 hour 35 minute', 55.00, 30, 0, 95, 'SCHEDULED', '11', 'T1'),
    ('f0000001-0000-0000-0000-000000000005', 'H25200', 2, 1, 3, NOW() + INTERVAL '1 day' + INTERVAL '13 hour', NOW() + INTERVAL '1 day' + INTERVAL '14 hour 30 minute', 42.00, 15, 0, 90, 'SCHEDULED', '05', 'T1'),
    ('f0000001-0000-0000-0000-000000000006', 'LA2205', 1, 1, 5, NOW() + INTERVAL '1 day' + INTERVAL '6 hour', NOW() + INTERVAL '1 day' + INTERVAL '7 hour 55 minute', 79.99, 25, 0, 115, 'SCHEDULED', '19', 'T1'),
    ('f0000001-0000-0000-0000-000000000007', 'JA3100', 3, 1, 4, NOW() + INTERVAL '1 day' + INTERVAL '11 hour', NOW() + INTERVAL '1 day' + INTERVAL '12 hour 10 minute', 32.50, 40, 0, 70, 'SCHEDULED', '02', 'T1'),
    -- Live status / FIDS board flights (today)
    ('f0000002-0000-0000-0000-000000000001', 'LA2010', 1, 1, 2, NOW() + INTERVAL '30 minute', NOW() + INTERVAL '1 hour 55 minute', 69.00, 5, 0, 85, 'BOARDING', '12', 'T1'),
    ('f0000002-0000-0000-0000-000000000002', 'H25001', 2, 1, 3, NOW() + INTERVAL '1 hour 15 minute', NOW() + INTERVAL '2 hour 45 minute', 52.00, 8, 0, 90, 'SCHEDULED', '07', 'T1'),
    ('f0000002-0000-0000-0000-000000000003', 'JA3002', 3, 1, 5, NOW() - INTERVAL '1 hour', NOW() + INTERVAL '55 minute', 45.00, 0, 0, 115, 'DELAYED', '04', 'T1')
ON CONFLICT (id) DO NOTHING;
