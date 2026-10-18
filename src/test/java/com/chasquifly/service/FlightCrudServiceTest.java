package com.chasquifly.service;

import com.chasquifly.dto.flight.CreateFlightDto;
import com.chasquifly.dto.flight.FlightResponseDto;
import com.chasquifly.exception.ResourceNotFoundException;
import com.chasquifly.model.Airline;
import com.chasquifly.model.Airport;
import com.chasquifly.model.Flight;
import com.chasquifly.repository.AirlineRepository;
import com.chasquifly.repository.AirportRepository;
import com.chasquifly.repository.FlightRepository;
import com.chasquifly.service.impl.FlightServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightCrudServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private AirportRepository airportRepository;

    @Mock
    private AirlineRepository airlineRepository;

    @InjectMocks
    private FlightServiceImpl flightService;

    private Airline sampleAirline;
    private Airport originAirport;
    private Airport destinationAirport;
    private Flight sampleFlight;
    private UUID flightId;

    @BeforeEach
    void setUp() {
        flightId = UUID.randomUUID();

        sampleAirline = Airline.builder()
                .id(1)
                .iataCode("LA")
                .name("LATAM Airlines")
                .logoUrl("https://example.com/latam.png")
                .build();

        originAirport = Airport.builder()
                .id(1)
                .iataCode("LIM")
                .name("Jorge Chávez")
                .city("Lima")
                .country("Perú")
                .timezone("America/Lima")
                .build();

        destinationAirport = Airport.builder()
                .id(2)
                .iataCode("CUZ")
                .name("Alejandro Velasco Astete")
                .city("Cusco")
                .country("Perú")
                .timezone("America/Lima")
                .build();

        sampleFlight = Flight.builder()
                .id(flightId)
                .flightNumber("LA2040")
                .airline(sampleAirline)
                .originAirport(originAirport)
                .destinationAirport(destinationAirport)
                .departureTime(OffsetDateTime.now().plusDays(1))
                .arrivalTime(OffsetDateTime.now().plusDays(1).plusHours(1))
                .basePrice(new BigDecimal("65.50"))
                .availableSeats(120)
                .stopsCount(0)
                .durationMinutes(60)
                .status("SCHEDULED")
                .gate("G4")
                .terminal("T1")
                .build();
    }

    @Test
    @DisplayName("Debe consultar un vuelo por su UUID con éxito")
    void testGetFlightById_Success() {
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(sampleFlight));

        FlightResponseDto response = flightService.getFlightById(flightId);

        assertNotNull(response);
        assertEquals(flightId, response.getFlightId());
        assertEquals("LA2040", response.getFlightNumber());
        assertEquals("LATAM Airlines", response.getAirline().getName());
        assertEquals("LIM", response.getOrigin().getIataCode());
        assertEquals("CUZ", response.getDestination().getIataCode());
        assertEquals(new BigDecimal("65.50"), response.getBasePrice());

        verify(flightRepository, times(1)).findById(flightId);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si el vuelo no existe")
    void testGetFlightById_NotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(flightRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> flightService.getFlightById(nonExistentId));
    }

    @Test
    @DisplayName("Debe crear un nuevo vuelo exitosamente vinculando aerolínea y aeropuertos")
    void testCreateFlight_Success() {
        CreateFlightDto dto = CreateFlightDto.builder()
                .flightNumber("LA2040")
                .airlineId(1)
                .originAirportId(1)
                .destinationAirportId(2)
                .departureTime(sampleFlight.getDepartureTime())
                .arrivalTime(sampleFlight.getArrivalTime())
                .basePrice(new BigDecimal("65.50"))
                .availableSeats(120)
                .stopsCount(0)
                .durationMinutes(60)
                .status("SCHEDULED")
                .gate("G4")
                .terminal("T1")
                .build();

        when(airlineRepository.findById(1)).thenReturn(Optional.of(sampleAirline));
        when(airportRepository.findById(1)).thenReturn(Optional.of(originAirport));
        when(airportRepository.findById(2)).thenReturn(Optional.of(destinationAirport));
        when(flightRepository.save(any(Flight.class))).thenReturn(sampleFlight);

        FlightResponseDto response = flightService.createFlight(dto);

        assertNotNull(response);
        assertEquals("LA2040", response.getFlightNumber());
        assertEquals("LIM", response.getOrigin().getIataCode());
        assertEquals("CUZ", response.getDestination().getIataCode());

        verify(airlineRepository, times(1)).findById(1);
        verify(airportRepository, times(1)).findById(1);
        verify(airportRepository, times(1)).findById(2);
        verify(flightRepository, times(1)).save(any(Flight.class));
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si la aerolínea referenciada no existe")
    void testCreateFlight_AirlineNotFound() {
        CreateFlightDto dto = CreateFlightDto.builder()
                .flightNumber("LA2040")
                .airlineId(999)
                .originAirportId(1)
                .destinationAirportId(2)
                .departureTime(sampleFlight.getDepartureTime())
                .arrivalTime(sampleFlight.getArrivalTime())
                .basePrice(new BigDecimal("65.50"))
                .durationMinutes(60)
                .build();

        when(airlineRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> flightService.createFlight(dto));
        verify(flightRepository, never()).save(any(Flight.class));
    }

    @Test
    @DisplayName("Debe actualizar un vuelo existente correctamente")
    void testUpdateFlight_Success() {
        CreateFlightDto dto = CreateFlightDto.builder()
                .flightNumber("LA2040-UPDATED")
                .airlineId(1)
                .originAirportId(1)
                .destinationAirportId(2)
                .departureTime(sampleFlight.getDepartureTime())
                .arrivalTime(sampleFlight.getArrivalTime())
                .basePrice(new BigDecimal("75.00"))
                .availableSeats(90)
                .stopsCount(0)
                .durationMinutes(65)
                .status("DELAYED")
                .build();

        when(flightRepository.findById(flightId)).thenReturn(Optional.of(sampleFlight));
        when(airlineRepository.findById(1)).thenReturn(Optional.of(sampleAirline));
        when(airportRepository.findById(1)).thenReturn(Optional.of(originAirport));
        when(airportRepository.findById(2)).thenReturn(Optional.of(destinationAirport));
        when(flightRepository.save(any(Flight.class))).thenReturn(sampleFlight);

        FlightResponseDto response = flightService.updateFlight(flightId, dto);

        assertNotNull(response);
        verify(flightRepository, times(1)).findById(flightId);
        verify(flightRepository, times(1)).save(sampleFlight);
    }

    @Test
    @DisplayName("Debe eliminar un vuelo por ID exitosamente")
    void testDeleteFlight_Success() {
        when(flightRepository.existsById(flightId)).thenReturn(true);
        doNothing().when(flightRepository).deleteById(flightId);

        assertDoesNotThrow(() -> flightService.deleteFlight(flightId));
        verify(flightRepository, times(1)).deleteById(flightId);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException al intentar eliminar un vuelo inexistente")
    void testDeleteFlight_NotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(flightRepository.existsById(nonExistentId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> flightService.deleteFlight(nonExistentId));
        verify(flightRepository, never()).deleteById(any());
    }
}
