package com.chasquifly.controller;

import com.chasquifly.dto.flight.CreateFlightDto;
import com.chasquifly.dto.flight.FlightComparisonDto;
import com.chasquifly.dto.flight.FlightResponseDto;
import com.chasquifly.dto.flight.FlightSearchCriteria;
import com.chasquifly.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para la búsqueda, comparación y gestión de vuelos.
 */
@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
@Tag(name = "3. Vuelos", description = "Búsqueda, comparación y CRUD de vuelos indexados")
public class FlightController {

    private final FlightService flightService;

    /**
     * Búsqueda multicriterio de vuelos con filtros dinámicos.
     *
     * @param criteria Parámetros de consulta (origen, destino, fecha, precios, escalas, aerolínea).
     * @return Lista de vuelos coincidentes.
     */
    @GetMapping({"", "/search"})
    @Operation(summary = "Buscar vuelos con filtros multicriterio",
        description = "Permite filtrar por origen, destino, fechas, precios máximos, aerolíneas y escalas. " +
                      "Todos los datos provienen de la base de datos PostgreSQL de ChasquiFly.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de vuelos que coinciden con los criterios",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = FlightResponseDto.class)))),
        @ApiResponse(responseCode = "400", description = "Parámetros de búsqueda inválidos",
            content = @Content(schema = @Schema()))
    })
    public ResponseEntity<List<FlightResponseDto>> searchFlights(@ModelAttribute FlightSearchCriteria criteria) {
        return ResponseEntity.ok(flightService.searchFlights(criteria));
    }

    /**
     * Consulta pública del detalle completo de un vuelo por su UUID.
     */
    @GetMapping("/{flightId}")
    @Operation(summary = "Obtener detalle de vuelo", description = "Obtiene información detallada de un vuelo por su UUID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detalle del vuelo encontrado",
            content = @Content(schema = @Schema(implementation = FlightResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Vuelo no encontrado con el UUID proporcionado",
            content = @Content(schema = @Schema()))
    })
    public ResponseEntity<FlightResponseDto> getFlightById(@PathVariable UUID flightId) {
        return ResponseEntity.ok(flightService.getFlightById(flightId));
    }

    /** Endpoint administrativo (ROLE_ADMIN) para registrar un nuevo vuelo. */
    @PostMapping
    @Operation(summary = "Crear nuevo vuelo (Admin)", description = "Registra un nuevo vuelo en la base de datos")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Vuelo creado exitosamente",
            content = @Content(schema = @Schema(implementation = FlightResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos del vuelo inválidos",
            content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "403", description = "Acceso denegado — se requiere rol ADMIN",
            content = @Content(schema = @Schema()))
    })
    public ResponseEntity<FlightResponseDto> createFlight(@Valid @RequestBody CreateFlightDto dto) {
        FlightResponseDto created = flightService.createFlight(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /** Endpoint administrativo (ROLE_ADMIN) para modificar un vuelo existente. */
    @PutMapping("/{flightId}")
    @Operation(summary = "Actualizar vuelo (Admin)", description = "Modifica los datos de un vuelo existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vuelo actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = FlightResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Vuelo no encontrado",
            content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "403", description = "Acceso denegado — se requiere rol ADMIN",
            content = @Content(schema = @Schema()))
    })
    public ResponseEntity<FlightResponseDto> updateFlight(
            @PathVariable UUID flightId,
            @Valid @RequestBody CreateFlightDto dto) {
        return ResponseEntity.ok(flightService.updateFlight(flightId, dto));
    }

    /** Endpoint administrativo (ROLE_ADMIN) para eliminar un vuelo por su UUID. */
    @DeleteMapping("/{flightId}")
    @Operation(summary = "Eliminar vuelo (Admin)", description = "Elimina un vuelo del sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Vuelo eliminado exitosamente — sin contenido"),
        @ApiResponse(responseCode = "404", description = "Vuelo no encontrado",
            content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "403", description = "Acceso denegado — se requiere rol ADMIN",
            content = @Content(schema = @Schema()))
    })
    public ResponseEntity<Void> deleteFlight(@PathVariable UUID flightId) {
        flightService.deleteFlight(flightId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/compare")
    @Operation(summary = "Comparar vuelos seleccionados",
        description = "Genera un análisis comparativo entre múltiples vuelos (más barato, más rápido, precios)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Comparación generada exitosamente",
            content = @Content(schema = @Schema(implementation = FlightComparisonDto.class))),
        @ApiResponse(responseCode = "400", description = "Lista de IDs vacía o inválida",
            content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "404", description = "Uno o más vuelos no encontrados",
            content = @Content(schema = @Schema()))
    })
    public ResponseEntity<FlightComparisonDto> compareFlights(@RequestBody List<UUID> flightIds) {
        return ResponseEntity.ok(flightService.compareFlights(flightIds));
    }

    /**
     * Obtiene una selección de ofertas y destinos populares para la página de inicio.
     *
     * @return Lista de vuelos destacados.
     */
    @GetMapping("/popular-destinations")
    @Operation(summary = "Destinos y ofertas populares",
        description = "Retorna una selección de vuelos destacados para la pantalla de inicio")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de vuelos populares",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = FlightResponseDto.class))))
    })
    public ResponseEntity<List<FlightResponseDto>> getPopularDestinations() {
        return ResponseEntity.ok(flightService.getPopularDestinations());
    }

    /**
     * Retorna una recomendación inteligente de vuelo basada en mejor relación calidad-precio.
     *
     * @param prompt Preferencias opcionales del usuario.
     * @return Vuelo recomendado.
     */
    @GetMapping("/ai-recommendation")
    @Operation(summary = "Recomendación asistida por IA",
        description = "Genera una recomendación inteligente basada en mejor relación calidad-precio")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vuelo recomendado por el sistema",
            content = @Content(schema = @Schema(implementation = FlightResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "No se encontró ningún vuelo disponible",
            content = @Content(schema = @Schema()))
    })
    public ResponseEntity<FlightResponseDto> getAiRecommendation(@RequestParam(required = false) String prompt) {
        return ResponseEntity.ok(flightService.getAiRecommendation(prompt));
    }
}
