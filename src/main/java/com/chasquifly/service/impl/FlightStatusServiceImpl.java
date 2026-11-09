package com.chasquifly.service.impl;

import com.chasquifly.dto.flight.FlightResponseDto;
import com.chasquifly.exception.ResourceNotFoundException;
import com.chasquifly.model.Flight;
import com.chasquifly.repository.FlightRepository;
import com.chasquifly.service.FlightService;
import com.chasquifly.service.FlightStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del servicio de estado operacional y tablero FIDS de ChasquiFly (RF-07, RF-08).
 * Orquesta consultas JPQL especializadas en PostgreSQL e hidrata cada vuelo con sus relaciones completas.
 */
@Service
@RequiredArgsConstructor
public class FlightStatusServiceImpl implements FlightStatusService {

    private final FlightRepository flightRepository;
    private final FlightService flightService;

    /**
     * Consulta las salidas ({@code DEPARTURES}) o llegadas ({@code ARRIVALS}) en vivo de un aeropuerto (RF-07).
     * Delega la recuperación a las queries JPQL {@code findLiveArrivals} y {@code findLiveDepartures}.
     */
    @Override
    @Transactional(readOnly = true)
    public List<FlightResponseDto> getLiveBoard(String airportCode, String type) {
        List<Flight> flights;
        // Conmutación entre tablero de llegadas y salidas según parámetro operativo
        if ("ARRIVALS".equalsIgnoreCase(type)) {
            flights = flightRepository.findLiveArrivals(airportCode);
        } else {
            flights = flightRepository.findLiveDepartures(airportCode);
        }

        // Hidratar DTOs completos (aerolínea, origen, destino, puerta y terminal)
        return flights.stream()
                .map(f -> flightService.getFlightById(f.getId()))
                .toList();
    }

    /**
     * Rastrea un vuelo individual mediante búsqueda insensible a mayúsculas por número de vuelo (RF-08).
     * Retorna puerta, terminal y estado operativo actualizado o lanza {@link ResourceNotFoundException}.
     */
    @Override
    @Transactional(readOnly = true)
    public FlightResponseDto trackFlight(String flightNumber) {
        // Búsqueda indexada insensible a mayúsculas/minúsculas sobre el código comercial del vuelo
        Flight flight = flightRepository.findByFlightNumberIgnoreCase(flightNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Vuelo no encontrado con número: " + flightNumber));
        // Delegar reconstrucción del DTO para exponer gate, terminal y horarios locales
        return flightService.getFlightById(flight.getId());
    }
}
