package com.chasquifly.controller;

import com.chasquifly.dto.airport.AirportDto;
import com.chasquifly.service.AirportService;
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
 * Controlador REST que publica los endpoints para la consulta pública de aeropuertos.
 * Ruta base: {@code /api/v1/airports}
 */
@RestController
@RequestMapping("/api/v1/airports")
@RequiredArgsConstructor
@Tag(name = "2. Aeropuertos", description = "Catálogo de aeropuertos y códigos IATA")
public class AirportController {

    private final AirportService airportService;

    /**
     * Endpoint GET /api/v1/airports
     * Retorna el catálogo completo de terminales aéreas disponibles.
     */
    @GetMapping
    @Operation(summary = "Listar aeropuertos", description = "Retorna todos los aeropuertos registrados en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Catálogo completo de aeropuertos",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AirportDto.class))))
    })
    public ResponseEntity<List<AirportDto>> getAllAirports() {
        return ResponseEntity.ok(airportService.getAllAirports());
    }

    /**
     * Endpoint GET /api/v1/airports/{iataCode}
     * Retorna los datos del aeropuerto solicitado por código IATA o 404 si no se encuentra.
     */
    @GetMapping("/{iataCode}")
    @Operation(summary = "Obtener aeropuerto por código IATA", description = "Consulta información de un aeropuerto por su código IATA de 3 letras (ej. LIM, CUZ)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Datos del aeropuerto encontrado",
            content = @Content(schema = @Schema(implementation = AirportDto.class))),
        @ApiResponse(responseCode = "404", description = "Aeropuerto no encontrado con ese código IATA",
            content = @Content(schema = @Schema()))
    })
    public ResponseEntity<AirportDto> getAirportByIataCode(@PathVariable String iataCode) {
        return ResponseEntity.ok(airportService.getAirportByIataCode(iataCode));
    }
}
