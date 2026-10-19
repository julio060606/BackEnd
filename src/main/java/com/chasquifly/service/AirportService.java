package com.chasquifly.service;

import com.chasquifly.dto.airport.AirportDto;

import java.util.List;

/**
 * Contrato de servicio para operaciones del catálogo de aeropuertos.
 */
public interface AirportService {

    /**
     * Recupera el catálogo completo de aeropuertos registrados en el sistema.
     *
     * @return Lista de {@link AirportDto} con la información de cada aeropuerto.
     */
    List<AirportDto> getAllAirports();

    /**
     * Busca y retorna los datos de un aeropuerto dado su código IATA de 3 letras.
     *
     * @param iataCode Código IATA (ej. "LIM", "CUZ").
     * @return {@link AirportDto} con los detalles encontrados.
     * @throws com.chasquifly.exception.ResourceNotFoundException Si el código no existe en la base de datos.
     */
    AirportDto getAirportByIataCode(String iataCode);
}
