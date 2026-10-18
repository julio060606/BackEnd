package com.chasquifly.service;

import com.chasquifly.dto.flight.FlightResponseDto;

import java.util.List;

public interface FlightStatusService {

    List<FlightResponseDto> getLiveBoard(String airportCode, String type);

    FlightResponseDto trackFlight(String flightNumber);
}
