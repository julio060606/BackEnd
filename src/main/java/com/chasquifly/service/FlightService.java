package com.chasquifly.service;

import com.chasquifly.dto.flight.CreateFlightDto;
import com.chasquifly.dto.flight.FlightComparisonDto;
import com.chasquifly.dto.flight.FlightResponseDto;
import com.chasquifly.dto.flight.FlightSearchCriteria;

import java.util.List;
import java.util.UUID;

public interface FlightService {

    List<FlightResponseDto> searchFlights(FlightSearchCriteria criteria);

    FlightResponseDto getFlightById(UUID flightId);

    FlightResponseDto createFlight(CreateFlightDto dto);

    FlightResponseDto updateFlight(UUID flightId, CreateFlightDto dto);

    void deleteFlight(UUID flightId);

    FlightComparisonDto compareFlights(List<UUID> flightIds);

    List<FlightResponseDto> getPopularDestinations();

    FlightResponseDto getAiRecommendation(String prompt);
}
