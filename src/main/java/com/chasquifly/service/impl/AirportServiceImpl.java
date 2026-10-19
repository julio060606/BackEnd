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

/**
 * Implementación de {@link AirportService} que interactúa con la capa de persistencia
 * a través de {@link AirportRepository}.
 */
@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;

    /**
     * Consulta y mapea todos los aeropuertos a formato DTO de forma transaccional de solo lectura.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AirportDto> getAllAirports() {
        return airportRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    /**
     * Localiza un aeropuerto por código IATA ignorando mayúsculas/minúsculas.
     * En caso de no existir, dispara una excepción {@link ResourceNotFoundException}.
     */
    @Override
    @Transactional(readOnly = true)
    public AirportDto getAirportByIataCode(String iataCode) {
        Airport airport = airportRepository.findByIataCodeIgnoreCase(iataCode)
                .orElseThrow(() -> new ResourceNotFoundException("Aeropuerto no encontrado con código IATA: " + iataCode));
        return mapToDto(airport);
    }

    /**
     * Transforma una entidad persistente {@link Airport} en su representación {@link AirportDto}.
     */
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
