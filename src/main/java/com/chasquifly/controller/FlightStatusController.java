package com.chasquifly.controller;

import com.chasquifly.dto.flight.FlightResponseDto;
import com.chasquifly.service.FlightStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST público para el tablero FIDS en vivo (RF-07) y seguimiento individual de vuelos (RF-08).
 * <p>
 * Ruta base: {@code /api/v1/flight-status}.
 * Expone endpoints documentados bajo OpenAPI 3 (RNF-05) para consultas operativas de salidas, llegadas y puertas.
 */
@RestController
@RequestMapping("/api/v1/flight-status")
@RequiredArgsConstructor
@Tag(name = "4. Estado y Seguimiento", description = "Tablero FIDS en vivo y seguimiento por número de vuelo")
public class FlightStatusController {

    /** Servicio encargado de resolver tableros FIDS por aeropuerto y rastreo por código comercial. */
    private final FlightStatusService flightStatusService;

    /**
     * Expone el tablero FIDS en tiempo real filtrado por código IATA de aeropuerto y tipo de operación (RF-07).
     *
     * @param airport Código IATA del aeropuerto a consultar (por defecto {@code LIM}).
     * @param type    Dirección operativa del tablero: {@code DEPARTURES} o {@code ARRIVALS}.
     * @return Lista de vuelos activos en formato {@link FlightResponseDto}.
     */
    @GetMapping("/live")
    @Operation(summary = "Tablero FIDS en tiempo real", description = "Lista salidas (DEPARTURES) o llegadas (ARRIVALS) para un aeropuerto específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tablero FIDS: lista de salidas o llegadas en tiempo real",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = FlightResponseDto.class)))),
        @ApiResponse(responseCode = "400", description = "Parámetro 'type' inválido (use DEPARTURES o ARRIVALS)",
            content = @Content(schema = @Schema()))
    })
    public ResponseEntity<List<FlightResponseDto>> getLiveBoard(
            @RequestParam(required = false, defaultValue = "LIM") String airport,
            @RequestParam(required = false, defaultValue = "DEPARTURES") String type) {
        return ResponseEntity.ok(flightStatusService.getLiveBoard(airport, type));
    }

    /**
     * Rastrea en tiempo real el estado operacional, puerta de embarque y terminal de un vuelo por código (RF-08).
     *
     * @param flightNumber Código comercial del vuelo (insensible a mayúsculas, ej. {@code LA2040}).
     * @return Detalle actualizado del vuelo o HTTP 404 si el código no existe en base de datos.
     */
    @GetMapping("/track/{flightNumber}")
    @Operation(summary = "Seguimiento por número de vuelo", description = "Rastrea el estado actual, puerta y terminal de un vuelo por su código (ej. LA2040)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado actual del vuelo (puerta, terminal, estado)",
            content = @Content(schema = @Schema(implementation = FlightResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Número de vuelo no encontrado en el sistema",
            content = @Content(schema = @Schema()))
    })
    public ResponseEntity<FlightResponseDto> trackFlight(@PathVariable String flightNumber) {
        return ResponseEntity.ok(flightStatusService.trackFlight(flightNumber));
    }
}
