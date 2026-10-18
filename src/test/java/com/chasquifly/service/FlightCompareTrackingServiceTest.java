package com.chasquifly.service;

import com.chasquifly.dto.airport.AirlineDto;
import com.chasquifly.dto.airport.AirportDto;
import com.chasquifly.dto.flight.FlightComparisonDto;
import com.chasquifly.dto.flight.FlightResponseDto;
import com.chasquifly.dto.trip.CreateTripDto;
import com.chasquifly.dto.trip.TripResponseDto;
import com.chasquifly.exception.ResourceNotFoundException;
import com.chasquifly.model.*;
import com.chasquifly.repository.*;
import com.chasquifly.service.impl.FlightServiceImpl;
import com.chasquifly.service.impl.FlightStatusServiceImpl;
import com.chasquifly.service.impl.TripServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightCompareTrackingServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private AirportRepository airportRepository;

    @Mock
    private AirlineRepository airlineRepository;

    @Mock
    private UserTripRepository userTripRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FlightService mockFlightService;

    @InjectMocks
    private FlightServiceImpl flightService;

    private FlightStatusServiceImpl flightStatusService;
    private TripServiceImpl tripService;

    private Flight flightA;
    private Flight flightB;
    private User sampleUser;
    private UUID flightAId;
    private UUID flightBId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        flightStatusService = new FlightStatusServiceImpl(flightRepository, mockFlightService);
        tripService = new TripServiceImpl(userTripRepository, userRepository, flightRepository, mockFlightService);

        flightAId = UUID.randomUUID();
        flightBId = UUID.randomUUID();
        userId = UUID.randomUUID();

        Airline latam = Airline.builder().id(1).iataCode("LA").name("LATAM").build();
        Airport lim = Airport.builder().id(1).iataCode("LIM").name("Jorge Chávez").city("Lima").country("Perú").build();
        Airport cuz = Airport.builder().id(2).iataCode("CUZ").name("Velasco Astete").city("Cusco").country("Perú").build();

        flightA = Flight.builder()
                .id(flightAId)
                .flightNumber("LA2040")
                .airline(latam)
                .originAirport(lim)
                .destinationAirport(cuz)
                .departureTime(OffsetDateTime.now())
                .arrivalTime(OffsetDateTime.now().plusHours(1))
                .basePrice(new BigDecimal("50.00"))
                .durationMinutes(60)
                .status("SCHEDULED")
                .build();

        flightB = Flight.builder()
                .id(flightBId)
                .flightNumber("LA2042")
                .airline(latam)
                .originAirport(lim)
                .destinationAirport(cuz)
                .departureTime(OffsetDateTime.now().plusHours(2))
                .arrivalTime(OffsetDateTime.now().plusHours(3).plusMinutes(30))
                .basePrice(new BigDecimal("90.00"))
                .durationMinutes(90)
                .status("BOARDING")
                .build();

        sampleUser = User.builder()
                .id(userId)
                .email("demo@chasquifly.com")
                .firstName("Pasajero")
                .lastName("Demo")
                .build();
    }

    @Test
    @DisplayName("Debe comparar múltiples vuelos calculando más barato, más rápido y diferencia")
    void testCompareFlights_Success() {
        when(flightRepository.findAllById(List.of(flightAId, flightBId))).thenReturn(List.of(flightA, flightB));

        FlightComparisonDto result = flightService.compareFlights(List.of(flightAId, flightBId));

        assertNotNull(result);
        assertEquals(2, result.getFlights().size());
        assertEquals(new BigDecimal("50.00"), result.getCheapestFlight().getBasePrice());
        assertEquals(60, result.getFastestFlight().getDurationMinutes());
        assertEquals(new BigDecimal("70.00"), result.getAveragePrice());
        assertEquals(new BigDecimal("40.00"), result.getPriceDifference());
    }

    @Test
    @DisplayName("Debe retornar lista vacía si no se encuentran vuelos al comparar")
    void testCompareFlights_Empty() {
        when(flightRepository.findAllById(any())).thenReturn(List.of());

        FlightComparisonDto result = flightService.compareFlights(List.of(UUID.randomUUID()));

        assertNotNull(result);
        assertTrue(result.getFlights().isEmpty());
    }

    @Test
    @DisplayName("Debe consultar salidas en vivo del tablero FIDS")
    void testGetLiveBoard_Departures() {
        FlightResponseDto mockDto = FlightResponseDto.builder().flightNumber("LA2040").status("SCHEDULED").build();
        when(flightRepository.findLiveDepartures("LIM")).thenReturn(List.of(flightA));
        when(mockFlightService.getFlightById(flightAId)).thenReturn(mockDto);

        List<FlightResponseDto> board = flightStatusService.getLiveBoard("LIM", "DEPARTURES");

        assertNotNull(board);
        assertEquals(1, board.size());
        assertEquals("LA2040", board.get(0).getFlightNumber());
    }

    @Test
    @DisplayName("Debe rastrear vuelo por su número")
    void testTrackFlight_Success() {
        FlightResponseDto mockDto = FlightResponseDto.builder().flightNumber("LA2040").status("SCHEDULED").build();
        when(flightRepository.findByFlightNumberIgnoreCase("LA2040")).thenReturn(Optional.of(flightA));
        when(mockFlightService.getFlightById(flightAId)).thenReturn(mockDto);

        FlightResponseDto tracked = flightStatusService.trackFlight("LA2040");

        assertNotNull(tracked);
        assertEquals("LA2040", tracked.getFlightNumber());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si el vuelo a rastrear no existe")
    void testTrackFlight_NotFound() {
        when(flightRepository.findByFlightNumberIgnoreCase("NONEXISTENT")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> flightStatusService.trackFlight("NONEXISTENT"));
    }

    @Test
    @DisplayName("Debe guardar un viaje en el itinerario del usuario")
    void testSaveTrip_Success() {
        CreateTripDto dto = new CreateTripDto(flightAId, "BOOK-123", "Viaje de vacaciones");
        UserTrip savedTrip = UserTrip.builder()
                .id(UUID.randomUUID())
                .user(sampleUser)
                .flight(flightA)
                .bookingReference("BOOK-123")
                .notes("Viaje de vacaciones")
                .build();

        FlightResponseDto mockDto = FlightResponseDto.builder().flightNumber("LA2040").build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(sampleUser));
        when(flightRepository.findById(flightAId)).thenReturn(Optional.of(flightA));
        when(userTripRepository.save(any(UserTrip.class))).thenReturn(savedTrip);
        when(mockFlightService.getFlightById(flightAId)).thenReturn(mockDto);

        TripResponseDto response = tripService.saveTrip(userId, dto);

        assertNotNull(response);
        assertEquals("BOOK-123", response.getBookingReference());
        assertEquals("Viaje de vacaciones", response.getNotes());
    }
}
