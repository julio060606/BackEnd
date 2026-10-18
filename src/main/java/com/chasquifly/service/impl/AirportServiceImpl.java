package com.chasquifly.service.impl;

import com.chasquifly.dto.airport.AirportDto;
import com.chasquifly.exception.ResourceNotFoundException;
import com.chasquifly.model.Airport;
import com.chasquifly.repository.AirportRepository;
import com.chasquifly.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AirportDto> getAllAirports() {
        return airportRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AirportDto getAirportByIataCode(String iataCode) {
        Airport airport = airportRepository.findByIataCodeIgnoreCase(iataCode)
                .orElseThrow(() -> new ResourceNotFoundException("Aeropuerto no encontrado con código IATA: " + iataCode));
        return mapToDto(airport);
    }

    private AirportDto mapToDto(Airport airport) {
        return AirportDto.builder()
                .id(airport.getId())
                .iataCode(airport.getIataCode())
                .name(airport.getName())
                .city(airport.getCity())
                .country(airport.getCountry())
                .timezone(airport.getTimezone())
                .build();
    }
}
