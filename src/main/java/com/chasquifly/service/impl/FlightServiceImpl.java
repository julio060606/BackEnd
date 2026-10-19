package com.chasquifly.service.impl;

import com.chasquifly.dto.airport.AirlineDto;
import com.chasquifly.dto.airport.AirportDto;
import com.chasquifly.dto.flight.*;
import com.chasquifly.exception.ResourceNotFoundException;
import com.chasquifly.model.Airline;
import com.chasquifly.model.Airport;
import com.chasquifly.model.Flight;
import com.chasquifly.repository.AirlineRepository;
import com.chasquifly.repository.AirportRepository;
import com.chasquifly.repository.FlightRepository;
import com.chasquifly.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final AirlineRepository airlineRepository;

    @Override
    @Transactional(readOnly = true)
    public List<FlightResponseDto> searchFlights(FlightSearchCriteria criteria) {
        OffsetDateTime startDate = null;
        OffsetDateTime endDate = null;

        if (criteria.getDepartureDate() != null) {
            startDate = criteria.getDepartureDate().atStartOfDay().atOffset(ZoneOffset.UTC);
            endDate = criteria.getDepartureDate().atTime(23, 59, 59).atOffset(ZoneOffset.UTC);
        }

        List<Flight> flights = flightRepository.searchFlights(
                criteria.getOrigin(),
                criteria.getDestination(),
                startDate,
                endDate
        );

        // Si no se encuentran vuelos en la fecha exacta, retornar los vuelos disponibles para esa ruta (búsqueda flexible)
        if (flights.isEmpty() && criteria.getOrigin() != null && criteria.getDestination() != null) {
            flights = flightRepository.searchFlights(
                    criteria.getOrigin(),
                    criteria.getDestination(),
                    null,
                    null
            );
        }

        // Apply in-memory secondary filters if provided
        return flights.stream()
                .filter(f -> criteria.getMaxPrice() == null || f.getBasePrice().compareTo(criteria.getMaxPrice()) <= 0)
                .filter(f -> criteria.getAirlineId() == null || f.getAirline().getId().equals(criteria.getAirlineId()))
                .filter(f -> criteria.getMaxStops() == null || f.getStopsCount() <= criteria.getMaxStops())
                .map(this::mapToFlightDto)
                .toList();
    }

    /**
     * Consulta un vuelo por su UUID o lanza ResourceNotFoundException si no existe.
     */
    @Override
    @Transactional(readOnly = true)
    public FlightResponseDto getFlightById(UUID flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Vuelo no encontrado con ID: " + flightId));
        return mapToFlightDto(flight);
    }

    /**
     * Registra un nuevo vuelo validando previamente la existencia de la aerolínea y los aeropuertos.
     */
    @Override
    @Transactional
    public FlightResponseDto createFlight(CreateFlightDto dto) {
        Airline airline = airlineRepository.findById(dto.getAirlineId())
                .orElseThrow(() -> new ResourceNotFoundException("Aerolínea no encontrada con ID: " + dto.getAirlineId()));

        Airport origin = airportRepository.findById(dto.getOriginAirportId())
                .orElseThrow(() -> new ResourceNotFoundException("Aeropuerto de origen no encontrado con ID: " + dto.getOriginAirportId()));

        Airport destination = airportRepository.findById(dto.getDestinationAirportId())
                .orElseThrow(() -> new ResourceNotFoundException("Aeropuerto de destino no encontrado con ID: " + dto.getDestinationAirportId()));

        Flight flight = Flight.builder()
                .flightNumber(dto.getFlightNumber())
                .airline(airline)
                .originAirport(origin)
                .destinationAirport(destination)
                .departureTime(dto.getDepartureTime())
                .arrivalTime(dto.getArrivalTime())
                .basePrice(dto.getBasePrice())
                .availableSeats(dto.getAvailableSeats() != null ? dto.getAvailableSeats() : 100)
                .stopsCount(dto.getStopsCount() != null ? dto.getStopsCount() : 0)
                .durationMinutes(dto.getDurationMinutes())
                .status(dto.getStatus() != null ? dto.getStatus() : "SCHEDULED")
                .gate(dto.getGate())
                .terminal(dto.getTerminal())
                .build();

        Flight savedFlight = flightRepository.save(flight);
        return mapToFlightDto(savedFlight);
    }

    /**
     * Actualiza los datos e itinerario de un vuelo existente por su UUID.
     */
    @Override
    @Transactional
    public FlightResponseDto updateFlight(UUID flightId, CreateFlightDto dto) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Vuelo no encontrado con ID: " + flightId));

        Airline airline = airlineRepository.findById(dto.getAirlineId())
                .orElseThrow(() -> new ResourceNotFoundException("Aerolínea no encontrada con ID: " + dto.getAirlineId()));

        Airport origin = airportRepository.findById(dto.getOriginAirportId())
                .orElseThrow(() -> new ResourceNotFoundException("Aeropuerto de origen no encontrado con ID: " + dto.getOriginAirportId()));

        Airport destination = airportRepository.findById(dto.getDestinationAirportId())
                .orElseThrow(() -> new ResourceNotFoundException("Aeropuerto de destino no encontrado con ID: " + dto.getDestinationAirportId()));

        flight.setFlightNumber(dto.getFlightNumber());
        flight.setAirline(airline);
        flight.setOriginAirport(origin);
        flight.setDestinationAirport(destination);
        flight.setDepartureTime(dto.getDepartureTime());
        flight.setArrivalTime(dto.getArrivalTime());
        flight.setBasePrice(dto.getBasePrice());
        flight.setAvailableSeats(dto.getAvailableSeats());
        flight.setStopsCount(dto.getStopsCount());
        flight.setDurationMinutes(dto.getDurationMinutes());
        flight.setStatus(dto.getStatus());
        flight.setGate(dto.getGate());
        flight.setTerminal(dto.getTerminal());

        Flight updatedFlight = flightRepository.save(flight);
        return mapToFlightDto(updatedFlight);
    }

    /**
     * Elimina un vuelo del sistema verificando previamente su existencia en la base de datos.
     */
    @Override
    @Transactional
    public void deleteFlight(UUID flightId) {
        if (!flightRepository.existsById(flightId)) {
            throw new ResourceNotFoundException("Vuelo no encontrado con ID: " + flightId);
        }
        flightRepository.deleteById(flightId);
    }

    @Override
    @Transactional(readOnly = true)
    public FlightComparisonDto compareFlights(List<UUID> flightIds) {
        List<Flight> flights = flightRepository.findAllById(flightIds);
        if (flights.isEmpty()) {
            return FlightComparisonDto.builder().flights(List.of()).build();
        }

        List<FlightResponseDto> dtos = flights.stream().map(this::mapToFlightDto).toList();

        Flight cheapest = flights.stream().min(Comparator.comparing(Flight::getBasePrice)).orElse(null);
        Flight fastest = flights.stream().min(Comparator.comparing(Flight::getDurationMinutes)).orElse(null);

        BigDecimal totalPrice = flights.stream()
                .map(Flight::getBasePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal averagePrice = totalPrice.divide(BigDecimal.valueOf(flights.size()), 2, RoundingMode.HALF_UP);

        BigDecimal maxPrice = flights.stream().map(Flight::getBasePrice).max(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);
        BigDecimal minPrice = cheapest != null ? cheapest.getBasePrice() : BigDecimal.ZERO;
        BigDecimal priceDifference = maxPrice.subtract(minPrice);

        return FlightComparisonDto.builder()
                .flights(dtos)
                .cheapestFlight(cheapest != null ? mapToFlightDto(cheapest) : null)
                .fastestFlight(fastest != null ? mapToFlightDto(fastest) : null)
                .averagePrice(averagePrice)
                .priceDifference(priceDifference)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightResponseDto> getPopularDestinations() {
        return flightRepository.findAll().stream()
                .limit(6)
                .map(this::mapToFlightDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FlightResponseDto getAiRecommendation(String prompt) {
        // Find best flight: lowest price or highest availability
        return flightRepository.findAll().stream()
                .min(Comparator.comparing(Flight::getBasePrice))
                .map(this::mapToFlightDto)
                .orElse(null);
    }

    private FlightResponseDto mapToFlightDto(Flight flight) {
        return FlightResponseDto.builder()
                .flightId(flight.getId())
                .flightNumber(flight.getFlightNumber())
                .airline(AirlineDto.builder()
                        .id(flight.getAirline().getId())
                        .iataCode(flight.getAirline().getIataCode())
                        .name(flight.getAirline().getName())
                        .logoUrl(flight.getAirline().getLogoUrl())
                        .build())
                .origin(AirportDto.builder()
                        .id(flight.getOriginAirport().getId())
                        .iataCode(flight.getOriginAirport().getIataCode())
                        .name(flight.getOriginAirport().getName())
                        .city(flight.getOriginAirport().getCity())
                        .country(flight.getOriginAirport().getCountry())
                        .timezone(flight.getOriginAirport().getTimezone())
                        .build())
                .destination(AirportDto.builder()
                        .id(flight.getDestinationAirport().getId())
                        .iataCode(flight.getDestinationAirport().getIataCode())
                        .name(flight.getDestinationAirport().getName())
                        .city(flight.getDestinationAirport().getCity())
                        .country(flight.getDestinationAirport().getCountry())
                        .timezone(flight.getDestinationAirport().getTimezone())
                        .build())
                .departureTime(flight.getDepartureTime())
                .arrivalTime(flight.getArrivalTime())
                .durationMinutes(flight.getDurationMinutes())
                .stopsCount(flight.getStopsCount())
                .basePrice(flight.getBasePrice())
                .currency("USD")
                .availableSeats(flight.getAvailableSeats())
                .status(flight.getStatus())
                .gate(flight.getGate())
                .terminal(flight.getTerminal())
                .build();
    }
}
