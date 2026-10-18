package com.chasquifly.controller;

import com.chasquifly.dto.flight.CreateFlightDto;
import com.chasquifly.dto.flight.FlightComparisonDto;
import com.chasquifly.dto.flight.FlightResponseDto;
import com.chasquifly.dto.flight.FlightSearchCriteria;
import com.chasquifly.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
@Tag(name = "3. Vuelos", description = "Búsqueda, comparación y CRUD de vuelos indexados")
public class FlightController {

    private final FlightService flightService;

    @GetMapping("/search")
    @Operation(summary = "Buscar vuelos con filtros multicriterio", description = "Permite filtrar por origen, destino, fechas, precios máximos, aerolíneas y escalas")
    public ResponseEntity<List<FlightResponseDto>> searchFlights(@ModelAttribute FlightSearchCriteria criteria) {
        return ResponseEntity.ok(flightService.searchFlights(criteria));
    }

    @GetMapping("/{flightId}")
    @Operation(summary = "Obtener detalle de vuelo", description = "Obtiene información detallada de un vuelo por su UUID")
    public ResponseEntity<FlightResponseDto> getFlightById(@PathVariable UUID flightId) {
        return ResponseEntity.ok(flightService.getFlightById(flightId));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo vuelo (Admin)", description = "Registra un nuevo vuelo en la base de datos")
    public ResponseEntity<FlightResponseDto> createFlight(@Valid @RequestBody CreateFlightDto dto) {
        FlightResponseDto created = flightService.createFlight(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{flightId}")
    @Operation(summary = "Actualizar vuelo (Admin)", description = "Modifica los datos de un vuelo existente")
    public ResponseEntity<FlightResponseDto> updateFlight(
            @PathVariable UUID flightId,
            @Valid @RequestBody CreateFlightDto dto) {
        return ResponseEntity.ok(flightService.updateFlight(flightId, dto));
    }

    @DeleteMapping("/{flightId}")
    @Operation(summary = "Eliminar vuelo (Admin)", description = "Elimina un vuelo del sistema")
    public ResponseEntity<Void> deleteFlight(@PathVariable UUID flightId) {
        flightService.deleteFlight(flightId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/compare")
    @Operation(summary = "Comparar vuelos seleccionados", description = "Genera un análisis comparativo entre múltiples vuelos (más barato, más rápido, precios)")
    public ResponseEntity<FlightComparisonDto> compareFlights(@RequestBody List<UUID> flightIds) {
        return ResponseEntity.ok(flightService.compareFlights(flightIds));
    }

    @GetMapping("/popular-destinations")
    @Operation(summary = "Destinos y ofertas populares", description = "Retorna una selección de vuelos destacados para la pantalla de inicio")
    public ResponseEntity<List<FlightResponseDto>> getPopularDestinations() {
        return ResponseEntity.ok(flightService.getPopularDestinations());
    }

    @GetMapping("/ai-recommendation")
    @Operation(summary = "Recomendación asistida por IA", description = "Genera una recomendación inteligente basada en mejor relación calidad-precio")
    public ResponseEntity<FlightResponseDto> getAiRecommendation(@RequestParam(required = false) String prompt) {
        return ResponseEntity.ok(flightService.getAiRecommendation(prompt));
    }
}
