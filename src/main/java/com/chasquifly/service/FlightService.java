package com.chasquifly.service;

import com.chasquifly.dto.flight.CreateFlightDto;
import com.chasquifly.dto.flight.FlightComparisonDto;
import com.chasquifly.dto.flight.FlightResponseDto;
import com.chasquifly.dto.flight.FlightSearchCriteria;

import java.util.List;
import java.util.UUID;

public interface FlightService {

    List<FlightResponseDto> searchFlights(FlightSearchCriteria criteria);

    /** Obtiene el detalle de un vuelo por su identificador UUID. */
    FlightResponseDto getFlightById(UUID flightId);

    /** Registra un nuevo vuelo validando sus relaciones de aerolínea y aeropuertos. */
    FlightResponseDto createFlight(CreateFlightDto dto);

    /** Actualiza los datos de un vuelo existente en el catálogo. */
    FlightResponseDto updateFlight(UUID flightId, CreateFlightDto dto);

    /** Elimina un vuelo existente por su identificador UUID. */
    void deleteFlight(UUID flightId);

    FlightComparisonDto compareFlights(List<UUID> flightIds);

    List<FlightResponseDto> getPopularDestinations();

    FlightResponseDto getAiRecommendation(String prompt);
}
