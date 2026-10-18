package com.chasquifly.service;

import com.chasquifly.dto.airport.AirlineDto;
import com.chasquifly.dto.airport.AirportDto;
import com.chasquifly.dto.flight.FlightResponseDto;
import com.chasquifly.dto.flight.FlightSearchCriteria;
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
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightSearchServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private AirportRepository airportRepository;

    @Mock
    private AirlineRepository airlineRepository;

    @InjectMocks
    private FlightServiceImpl flightService;

    private Flight flight1;
    private Flight flight2;
    private Airline latam;
    private Airline sky;
    private Airport lim;
    private Airport cuz;

    @BeforeEach
    void setUp() {
        latam = Airline.builder().id(1).iataCode("LA").name("LATAM Airlines").build();
        sky = Airline.builder().id(2).iataCode("H2").name("SKY Airline").build();

        lim = Airport.builder().id(1).iataCode("LIM").name("Jorge Chávez").city("Lima").country("Perú").build();
        cuz = Airport.builder().id(2).iataCode("CUZ").name("Velasco Astete").city("Cusco").country("Perú").build();

        flight1 = Flight.builder()
                .id(UUID.randomUUID())
                .flightNumber("LA2040")
                .airline(latam)
                .originAirport(lim)
                .destinationAirport(cuz)
                .departureTime(OffsetDateTime.now().plusDays(2))
                .arrivalTime(OffsetDateTime.now().plusDays(2).plusHours(1))
                .basePrice(new BigDecimal("55.00"))
                .availableSeats(100)
                .stopsCount(0)
                .durationMinutes(65)
                .status("SCHEDULED")
                .build();

        flight2 = Flight.builder()
                .id(UUID.randomUUID())
                .flightNumber("H25100")
                .airline(sky)
                .originAirport(lim)
                .destinationAirport(cuz)
                .departureTime(OffsetDateTime.now().plusDays(2))
                .arrivalTime(OffsetDateTime.now().plusDays(2).plusHours(2))
                .basePrice(new BigDecimal("85.00"))
                .availableSeats(80)
                .stopsCount(1)
                .durationMinutes(120)
                .status("SCHEDULED")
                .build();
    }

    @Test
    @DisplayName("Debe buscar vuelos con criterios de origen y destino")
    void testSearchFlights_Success() {
        FlightSearchCriteria criteria = new FlightSearchCriteria();
        criteria.setOrigin("LIM");
        criteria.setDestination("CUZ");
        criteria.setDepartureDate(LocalDate.now().plusDays(2));

        when(flightRepository.searchFlights(eq("LIM"), eq("CUZ"), any(), any()))
                .thenReturn(List.of(flight1, flight2));

        List<FlightResponseDto> results = flightService.searchFlights(criteria);

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("LA2040", results.get(0).getFlightNumber());
        assertEquals("H25100", results.get(1).getFlightNumber());
    }

    @Test
    @DisplayName("Debe filtrar vuelos por precio máximo")
    void testSearchFlights_FilterByMaxPrice() {
        FlightSearchCriteria criteria = new FlightSearchCriteria();
        criteria.setOrigin("LIM");
        criteria.setDestination("CUZ");
        criteria.setMaxPrice(new BigDecimal("60.00"));

        when(flightRepository.searchFlights(eq("LIM"), eq("CUZ"), any(), any()))
                .thenReturn(List.of(flight1, flight2));

        List<FlightResponseDto> results = flightService.searchFlights(criteria);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("LA2040", results.get(0).getFlightNumber());
    }

    @Test
    @DisplayName("Debe filtrar vuelos por aerolínea específica")
    void testSearchFlights_FilterByAirline() {
        FlightSearchCriteria criteria = new FlightSearchCriteria();
        criteria.setAirlineId(2);

        when(flightRepository.searchFlights(any(), any(), any(), any()))
                .thenReturn(List.of(flight1, flight2));

        List<FlightResponseDto> results = flightService.searchFlights(criteria);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("SKY Airline", results.get(0).getAirline().getName());
    }

    @Test
    @DisplayName("Debe filtrar vuelos por cantidad máxima de escalas")
    void testSearchFlights_FilterByMaxStops() {
        FlightSearchCriteria criteria = new FlightSearchCriteria();
        criteria.setMaxStops(0); // Vuelos directos

        when(flightRepository.searchFlights(any(), any(), any(), any()))
                .thenReturn(List.of(flight1, flight2));

        List<FlightResponseDto> results = flightService.searchFlights(criteria);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(0, results.get(0).getStopsCount());
    }

    @Test
    @DisplayName("Debe obtener destinos populares")
    void testGetPopularDestinations() {
        when(flightRepository.findAll()).thenReturn(List.of(flight1, flight2));

        List<FlightResponseDto> popular = flightService.getPopularDestinations();

        assertNotNull(popular);
        assertEquals(2, popular.size());
    }

    @Test
    @DisplayName("Debe retornar la mejor recomendación asistida por IA (menor precio)")
    void testGetAiRecommendation() {
        when(flightRepository.findAll()).thenReturn(List.of(flight2, flight1));

        FlightResponseDto recommendation = flightService.getAiRecommendation("mejor precio a Cusco");

        assertNotNull(recommendation);
        assertEquals("LA2040", recommendation.getFlightNumber());
        assertEquals(new BigDecimal("55.00"), recommendation.getBasePrice());
    }
}
