package com.chasquifly.service;

import com.chasquifly.dto.flight.FlightResponseDto;

import java.util.List;

/**
 * Contrato de servicio para operaciones del tablero FIDS en tiempo real (RF-07)
 * y seguimiento individual por código de vuelo comercial (RF-08).
 */
public interface FlightStatusService {

    /**
     * Obtiene el tablero informativo de vuelos (FIDS) para un aeropuerto según dirección operativa.
     *
     * @param airportCode Código IATA de 3 letras del aeropuerto (ej. {@code LIM}, {@code CUZ}).
     * @param type        Tipo de movimiento operativo: {@code DEPARTURES} o {@code ARRIVALS}.
     * @return Lista de vuelos activos formateados como {@link FlightResponseDto}.
     */
    List<FlightResponseDto> getLiveBoard(String airportCode, String type);

    /**
     * Rastrea el estado operativo, puerta y terminal de un vuelo a partir de su código comercial.
     *
     * @param flightNumber Código comercial del vuelo (ej. {@code LA2040}).
     * @return Detalle actualizado del vuelo encontrado.
     */
    FlightResponseDto trackFlight(String flightNumber);
}
