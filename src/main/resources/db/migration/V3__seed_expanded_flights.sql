-- ==============================================================================
-- Flyway Migration: V3__seed_expanded_flights.sql
-- Description: Expand Peruvian domestic flight network and inter-city routes
-- ==============================================================================

INSERT INTO flights (id, flight_number, airline_id, origin_airport_id, destination_airport_id, departure_time, arrival_time, base_price, available_seats, stops_count, duration_minutes, status, gate, terminal)
VALUES
    -- 1. Rutas de Retorno hacia Lima
    ('f0000003-0000-0000-0000-000000000001', 'LA2041', 1, 2, 1, NOW() + INTERVAL '1 day' + INTERVAL '10 hour', NOW() + INTERVAL '1 day' + INTERVAL '11 hour 20 minute', 59.99, 40, 0, 80, 'SCHEDULED', '04', 'T1'),
    ('f0000003-0000-0000-0000-000000000002', 'H25103', 2, 2, 1, NOW() + INTERVAL '1 day' + INTERVAL '16 hour', NOW() + INTERVAL '1 day' + INTERVAL '17 hour 25 minute', 47.00, 22, 0, 85, 'SCHEDULED', '06', 'T1'),
    ('f0000003-0000-0000-0000-000000000003', 'JA3011', 3, 2, 1, NOW() + INTERVAL '2 day' + INTERVAL '8 hour',  NOW() + INTERVAL '2 day' + INTERVAL '9 hour 20 minute',  38.00, 18, 0, 80, 'SCHEDULED', '02', 'T1'),
    ('f0000003-0000-0000-0000-000000000004', 'LA2111', 1, 3, 1, NOW() + INTERVAL '1 day' + INTERVAL '11 hour', NOW() + INTERVAL '1 day' + INTERVAL '12 hour 30 minute', 54.00, 35, 0, 90, 'SCHEDULED', '09', 'T1'),
    ('f0000003-0000-0000-0000-000000000005', 'H25201', 2, 3, 1, NOW() + INTERVAL '2 day' + INTERVAL '15 hour', NOW() + INTERVAL '2 day' + INTERVAL '16 hour 35 minute', 41.50, 28, 0, 95, 'SCHEDULED', '07', 'T1'),
    ('f0000003-0000-0000-0000-000000000006', 'LA2206', 1, 5, 1, NOW() + INTERVAL '1 day' + INTERVAL '13 hour', NOW() + INTERVAL '1 day' + INTERVAL '14 hour 50 minute', 78.50, 16, 0, 110, 'SCHEDULED', '01', 'T1'),
    ('f0000003-0000-0000-0000-000000000007', 'JA3101', 3, 4, 1, NOW() + INTERVAL '1 day' + INTERVAL '15 hour', NOW() + INTERVAL '1 day' + INTERVAL '16 hour 10 minute', 34.00, 32, 0, 70, 'SCHEDULED', '03', 'T1'),

    -- 2. Rutas del Norte (Piura y Tumbes)
    ('f0000003-0000-0000-0000-000000000008', 'LA2300', 1, 1, 6, NOW() + INTERVAL '1 day' + INTERVAL '7 hour',  NOW() + INTERVAL '1 day' + INTERVAL '8 hour 45 minute',  63.50, 48, 0, 105, 'SCHEDULED', '15', 'T1'),
    ('f0000003-0000-0000-0000-000000000009', 'LA2301', 1, 6, 1, NOW() + INTERVAL '2 day' + INTERVAL '12 hour', NOW() + INTERVAL '2 day' + INTERVAL '13 hour 40 minute', 62.00, 30, 0, 100, 'SCHEDULED', '02', 'T1'),
    ('f0000003-0000-0000-0000-000000000010', 'LA2400', 1, 1, 7, NOW() + INTERVAL '1 day' + INTERVAL '11 hour', NOW() + INTERVAL '1 day' + INTERVAL '13 hour 00 minute', 85.00, 25, 0, 120, 'SCHEDULED', '17', 'T1'),
    ('f0000003-0000-0000-0000-000000000011', 'LA2401', 1, 7, 1, NOW() + INTERVAL '2 day' + INTERVAL '14 hour', NOW() + INTERVAL '2 day' + INTERVAL '15 hour 55 minute', 83.00, 20, 0, 115, 'SCHEDULED', '01', 'T1'),

    -- 3. Conexiones Interregionales (Sur: Cusco <-> Arequipa)
    ('f0000003-0000-0000-0000-000000000012', 'H25300', 2, 2, 3, NOW() + INTERVAL '1 day' + INTERVAL '9 hour',  NOW() + INTERVAL '1 day' + INTERVAL '9 hour 55 minute',  45.00, 30, 0, 55, 'SCHEDULED', '05', 'T1'),
    ('f0000003-0000-0000-0000-000000000013', 'H25301', 2, 3, 2, NOW() + INTERVAL '1 day' + INTERVAL '16 hour', NOW() + INTERVAL '1 day' + INTERVAL '16 hour 55 minute', 46.50, 35, 0, 55, 'SCHEDULED', '08', 'T1'),

    -- 4. Vuelos con 1 Escala (Conexión Sur - Selva vía Lima)
    ('f0000003-0000-0000-0000-000000000014', 'LA2501', 1, 3, 5, NOW() + INTERVAL '1 day' + INTERVAL '8 hour',  NOW() + INTERVAL '1 day' + INTERVAL '12 hour 10 minute', 119.00, 15, 1, 250, 'SCHEDULED', '10', 'T1'),
    ('f0000003-0000-0000-0000-000000000015', 'LA2502', 1, 2, 5, NOW() + INTERVAL '1 day' + INTERVAL '7 hour',  NOW() + INTERVAL '1 day' + INTERVAL '11 hour 00 minute', 125.00, 14, 1, 240, 'SCHEDULED', '03', 'T1'),
    ('f0000003-0000-0000-0000-000000000016', 'LA2503', 1, 5, 3, NOW() + INTERVAL '2 day' + INTERVAL '10 hour', NOW() + INTERVAL '2 day' + INTERVAL '14 hour 40 minute', 118.00, 18, 1, 260, 'SCHEDULED', '02', 'T1')
ON CONFLICT (id) DO NOTHING;
