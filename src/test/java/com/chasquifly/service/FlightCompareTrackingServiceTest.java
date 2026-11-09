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

/**
 * Suite de 6 pruebas unitarias JUnit 5 + Mockito para el módulo Integrante 6 (RNF-06).
 * <p>
 * Verifica de forma aislada (sin base de datos física) la lógica de negocio de:
 * <ul>
 *   <li>Comparador analítico de vuelos en {@link FlightServiceImpl} (RF-06).</li>
 *   <li>Tablero FIDS y rastreo por código en {@link FlightStatusServiceImpl} (RF-07, RF-08).</li>
 *   <li>Registro de itinerarios personales en {@link TripServiceImpl} (RF-09).</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class FlightCompareTrackingServiceTest {

    // --- Repositorios y colaboradores simulados mediante Mockito ---
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

    // --- Servicios bajo prueba (SUT) ---
    @InjectMocks
    private FlightServiceImpl flightService;

    private FlightStatusServiceImpl flightStatusService;
    private TripServiceImpl tripService;

    // --- Fixtures compartidos entre escenarios de prueba ---
    private Flight flightA;
    private Flight flightB;
    private User sampleUser;
    private UUID flightAId;
    private UUID flightBId;
    private UUID userId;

    /**
     * Inicializa los servicios FIDS/itinerarios y construye entidades de vuelo y usuario de prueba antes de cada caso.
     */
    @BeforeEach
    void setUp() {
        // Instanciación explícita con repositorios mockeados para aislar FlightStatusService y TripService
        flightStatusService = new FlightStatusServiceImpl(flightRepository, mockFlightService);
        tripService = new TripServiceImpl(userTripRepository, userRepository, flightRepository, mockFlightService);

        flightAId = UUID.randomUUID();
        flightBId = UUID.randomUUID();
        userId = UUID.randomUUID();

        // Catálogo maestro en memoria: aerolínea LATAM y ruta doméstica LIM -> CUZ
        Airline latam = Airline.builder().id(1).iataCode("LA").name("LATAM").build();
        Airport lim = Airport.builder().id(1).iataCode("LIM").name("Jorge Chávez").city("Lima").country("Perú").build();
        Airport cuz = Airport.builder().id(2).iataCode("CUZ").name("Velasco Astete").city("Cusco").country("Perú").build();

        // Vuelo A: opción más económica ($50.00) y rápida (60 min)
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

        // Vuelo B: opción de mayor tarifa ($90.00) y duración (90 min) para validar métricas comparativas
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

    /** Prueba 1/6 (RF-06): Certifica el cálculo financiero exacto en comparación multicriterio. */
    @Test
    @DisplayName("Debe comparar múltiples vuelos calculando más barato, más rápido y diferencia")
    void testCompareFlights_Success() {
        // Given: dos vuelos existentes con tarifas $50.00 (60m) y $90.00 (90m)
        when(flightRepository.findAllById(List.of(flightAId, flightBId))).thenReturn(List.of(flightA, flightB));

        // When: se ejecuta el comparador analítico
        FlightComparisonDto result = flightService.compareFlights(List.of(flightAId, flightBId));

        // Then: valida más barato, más rápido, promedio ($70.00) y ahorro neto ($40.00)
        assertNotNull(result);
        assertEquals(2, result.getFlights().size());
        assertEquals(new BigDecimal("50.00"), result.getCheapestFlight().getBasePrice());
        assertEquals(60, result.getFastestFlight().getDurationMinutes());
        assertEquals(new BigDecimal("70.00"), result.getAveragePrice());
        assertEquals(new BigDecimal("40.00"), result.getPriceDifference());
    }

    /** Prueba 2/6 (RF-06): Certifica respuesta segura vacía cuando ningún UUID comparado existe. */
    @Test
    @DisplayName("Debe retornar lista vacía si no se encuentran vuelos al comparar")
    void testCompareFlights_Empty() {
        // Given: repositorio sin coincidencias para el UUID consultado
        when(flightRepository.findAllById(any())).thenReturn(List.of());

        // When: se invoca la comparación con ID inexistente
        FlightComparisonDto result = flightService.compareFlights(List.of(UUID.randomUUID()));

        // Then: retorna objeto DTO no nulo con colección vacía sin lanzar excepción
        assertNotNull(result);
        assertTrue(result.getFlights().isEmpty());
    }

    /** Prueba 3/6 (RF-07): Certifica la consulta en vivo de salidas en el tablero FIDS. */
    @Test
    @DisplayName("Debe consultar salidas en vivo del tablero FIDS")
    void testGetLiveBoard_Departures() {
        // Given: salida activa programada desde el aeropuerto LIM
        FlightResponseDto mockDto = FlightResponseDto.builder().flightNumber("LA2040").status("SCHEDULED").build();
        when(flightRepository.findLiveDepartures("LIM")).thenReturn(List.of(flightA));
        when(mockFlightService.getFlightById(flightAId)).thenReturn(mockDto);

        // When: se consulta el tablero FIDS en modo DEPARTURES
        List<FlightResponseDto> board = flightStatusService.getLiveBoard("LIM", "DEPARTURES");

        // Then: retorna la salida esperada mapeada por el servicio de vuelos
        assertNotNull(board);
        assertEquals(1, board.size());
        assertEquals("LA2040", board.get(0).getFlightNumber());
    }

    /** Prueba 4/6 (RF-08): Certifica el rastreo individual exitoso por código comercial. */
    @Test
    @DisplayName("Debe rastrear vuelo por su número")
    void testTrackFlight_Success() {
        // Given: vuelo LA2040 indexado y recuperable por código comercial
        FlightResponseDto mockDto = FlightResponseDto.builder().flightNumber("LA2040").status("SCHEDULED").build();
        when(flightRepository.findByFlightNumberIgnoreCase("LA2040")).thenReturn(Optional.of(flightA));
        when(mockFlightService.getFlightById(flightAId)).thenReturn(mockDto);

        // When: se rastrea el código LA2040
        FlightResponseDto tracked = flightStatusService.trackFlight("LA2040");

        // Then: retorna el detalle operacional del vuelo coincidente
        assertNotNull(tracked);
        assertEquals("LA2040", tracked.getFlightNumber());
    }

    /** Prueba 5/6 (RF-08): Certifica el lanzamiento de ResourceNotFoundException ante código inexistente. */
    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si el vuelo a rastrear no existe")
    void testTrackFlight_NotFound() {
        // Given: código de vuelo sin coincidencias en base de datos
        when(flightRepository.findByFlightNumberIgnoreCase("NONEXISTENT")).thenReturn(Optional.empty());

        // When / Then: el servicio debe propagar ResourceNotFoundException (HTTP 404)
        assertThrows(ResourceNotFoundException.class, () -> flightStatusService.trackFlight("NONEXISTENT"));
    }

    /** Prueba 6/6 (RF-09): Certifica el registro de un viaje con referencia y notas en el itinerario. */
    @Test
    @DisplayName("Debe guardar un viaje en el itinerario del usuario")
    void testSaveTrip_Success() {
        // Given: solicitud válida con código de reserva BOOK-123 y notas personalizadas
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

        // When: se persiste el viaje en el itinerario del usuario autenticado
        TripResponseDto response = tripService.saveTrip(userId, dto);

        // Then: confirma que referencia de reserva y notas quedaron registradas correctamente
        assertNotNull(response);
        assertEquals("BOOK-123", response.getBookingReference());
        assertEquals("Viaje de vacaciones", response.getNotes());
    }
}
