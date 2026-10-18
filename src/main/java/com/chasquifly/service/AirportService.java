package com.chasquifly.service;

import com.chasquifly.dto.airport.AirportDto;

import java.util.List;

public interface AirportService {

    List<AirportDto> getAllAirports();

    AirportDto getAirportByIataCode(String iataCode);
}
