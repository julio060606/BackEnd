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

@Service
@RequiredArgsConstructor
public class FlightStatusServiceImpl implements FlightStatusService {

    private final FlightRepository flightRepository;
    private final FlightService flightService;

    @Override
    @Transactional(readOnly = true)
    public List<FlightResponseDto> getLiveBoard(String airportCode, String type) {
        List<Flight> flights;
        if ("ARRIVALS".equalsIgnoreCase(type)) {
            flights = flightRepository.findLiveArrivals(airportCode);
        } else {
            flights = flightRepository.findLiveDepartures(airportCode);
        }

        return flights.stream()
                .map(f -> flightService.getFlightById(f.getId()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FlightResponseDto trackFlight(String flightNumber) {
        Flight flight = flightRepository.findByFlightNumberIgnoreCase(flightNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Vuelo no encontrado con número: " + flightNumber));
        return flightService.getFlightById(flight.getId());
    }
}
