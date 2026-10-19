package com.chasquifly.service;

import com.chasquifly.dto.airport.AirportDto;
import com.chasquifly.exception.ResourceNotFoundException;
import com.chasquifly.model.Airport;
import com.chasquifly.repository.AirportRepository;
import com.chasquifly.service.impl.AirportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Suite de pruebas unitarias para {@link AirportServiceImpl}.
 * Valida la lógica de negocio, mapeos a DTO y manejo de excepciones con Mockito.
 */
@ExtendWith(MockitoExtension.class)
class AirportServiceTest {

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private AirportServiceImpl airportService;

    private Airport limAirport;
    private Airport cuzAirport;

    @BeforeEach
    void setUp() {
        limAirport = Airport.builder()
                .id(1)
                .iataCode("LIM")
                .name("Aeropuerto Internacional Jorge Chávez")
                .city("Lima")
                .country("Perú")
                .timezone("America/Lima")
                .build();

        cuzAirport = Airport.builder()
                .id(2)
                .iataCode("CUZ")
                .name("Aeropuerto Internacional Alejandro Velasco Astete")
                .city("Cusco")
                .country("Perú")
                .timezone("America/Lima")
                .build();
    }

    /**
     * Valida que el servicio consulte la totalidad de aeropuertos del repositorio y los mapee a DTO.
     */
    @Test
    @DisplayName("Debe listar todos los aeropuertos registrados correctamente")
    void testGetAllAirports_Success() {
        when(airportRepository.findAll()).thenReturn(List.of(limAirport, cuzAirport));

        List<AirportDto> result = airportService.getAllAirports();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("LIM", result.get(0).getIataCode());
        assertEquals("Lima", result.get(0).getCity());
        assertEquals("CUZ", result.get(1).getIataCode());
        assertEquals("Cusco", result.get(1).getCity());

        verify(airportRepository, times(1)).findAll();
    }

    /**
     * Valida la búsqueda exitosa por código IATA tolerando minúsculas y mayúsculas.
     */
    @Test
    @DisplayName("Debe obtener un aeropuerto por su código IATA existente de forma insensible a mayúsculas")
    void testGetAirportByIataCode_Success() {
        when(airportRepository.findByIataCodeIgnoreCase("lim")).thenReturn(Optional.of(limAirport));

        AirportDto result = airportService.getAirportByIataCode("lim");

        assertNotNull(result);
        assertEquals("LIM", result.getIataCode());
        assertEquals("Lima", result.getCity());
        assertEquals("Aeropuerto Internacional Jorge Chávez", result.getName());

        verify(airportRepository, times(1)).findByIataCodeIgnoreCase("lim");
    }

    /**
     * Valida que se arroje ResourceNotFoundException si el código solicitado no está registrado.
     */
    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si el código IATA no existe")
    void testGetAirportByIataCode_NotFound() {
        when(airportRepository.findByIataCodeIgnoreCase("XXX")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> airportService.getAirportByIataCode("XXX"));
        verify(airportRepository, times(1)).findByIataCodeIgnoreCase("XXX");
    }
}
