package com.chasquifly.service;

import com.chasquifly.dto.flight.CreateFlightDto;
import com.chasquifly.dto.flight.FlightComparisonDto;
import com.chasquifly.dto.flight.FlightResponseDto;
import com.chasquifly.dto.flight.FlightSearchCriteria;

import java.util.List;
import java.util.UUID;

/**
 * Servicio de lógica de negocio para la búsqueda, gestión y análisis de vuelos.
 */
public interface FlightService {

    /**
     * Búsqueda multicriterio de vuelos basada en los filtros provistos en criteria.
     *
     * @param criteria Filtros de búsqueda (origen, destino, fecha, precio, escalas, aerolínea).
     * @return Lista de vuelos coincidentes.
     */
    List<FlightResponseDto> searchFlights(FlightSearchCriteria criteria);

    /**
     * Consulta el detalle de un vuelo por su UUID.
     *
     * @param flightId Identificador UUID del vuelo.
     * @return Detalle del vuelo.
     */
    FlightResponseDto getFlightById(UUID flightId);

    /**
     * Registra un nuevo vuelo en el catálogo.
     *
     * @param dto Datos del nuevo vuelo.
     * @return Vuelo registrado con ID asignado.
     */
    FlightResponseDto createFlight(CreateFlightDto dto);

    /**
     * Actualiza los datos de un vuelo existente.
     *
     * @param flightId Identificador UUID del vuelo.
     * @param dto      Datos a actualizar.
     * @return Vuelo actualizado.
     */
    FlightResponseDto updateFlight(UUID flightId, CreateFlightDto dto);

    /**
     * Elimina un vuelo por su UUID.
     *
     * @param flightId Identificador UUID del vuelo.
     */
    void deleteFlight(UUID flightId);

    /**
     * Genera un análisis comparativo entre una lista de vuelos seleccionados.
     *
     * @param flightIds Lista de identificadores UUID.
     * @return DTO con métricas y comparación de vuelos.
     */
    FlightComparisonDto compareFlights(List<UUID> flightIds);

    /**
     * Retorna vuelos destacados para la vista principal.
     *
     * @return Lista de hasta 6 vuelos de muestra.
     */
    List<FlightResponseDto> getPopularDestinations();

    /**
     * Genera una recomendación inteligente de vuelo basada en mejor relación calidad-precio.
     *
     * @param prompt Consulta opcional del usuario.
     * @return Vuelo recomendado o null.
     */
    FlightResponseDto getAiRecommendation(String prompt);
}
