package com.chasquifly.controller;

import com.chasquifly.dto.flight.FlightResponseDto;
import com.chasquifly.service.FlightStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flight-status")
@RequiredArgsConstructor
@Tag(name = "4. Estado y Seguimiento", description = "Tablero FIDS en vivo y seguimiento por número de vuelo")
public class FlightStatusController {

    private final FlightStatusService flightStatusService;

    @GetMapping("/live")
    @Operation(summary = "Tablero FIDS en tiempo real", description = "Lista salidas (DEPARTURES) o llegadas (ARRIVALS) para un aeropuerto específico")
    public ResponseEntity<List<FlightResponseDto>> getLiveBoard(
            @RequestParam(required = false, defaultValue = "LIM") String airport,
            @RequestParam(required = false, defaultValue = "DEPARTURES") String type) {
        return ResponseEntity.ok(flightStatusService.getLiveBoard(airport, type));
    }

    @GetMapping("/track/{flightNumber}")
    @Operation(summary = "Seguimiento por número de vuelo", description = "Rastrea el estado actual, puerta y terminal de un vuelo por su código (ej. LA2040)")
    public ResponseEntity<FlightResponseDto> trackFlight(@PathVariable String flightNumber) {
        return ResponseEntity.ok(flightStatusService.trackFlight(flightNumber));
    }
}
