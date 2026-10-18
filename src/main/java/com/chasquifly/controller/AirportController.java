package com.chasquifly.controller;

import com.chasquifly.dto.airport.AirportDto;
import com.chasquifly.service.AirportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/airports")
@RequiredArgsConstructor
@Tag(name = "2. Aeropuertos", description = "Catálogo de aeropuertos y códigos IATA")
public class AirportController {

    private final AirportService airportService;

    @GetMapping
    @Operation(summary = "Listar aeropuertos", description = "Retorna todos los aeropuertos registrados en el sistema")
    public ResponseEntity<List<AirportDto>> getAllAirports() {
        return ResponseEntity.ok(airportService.getAllAirports());
    }

    @GetMapping("/{iataCode}")
    @Operation(summary = "Obtener aeropuerto por código IATA", description = "Consulta información de un aeropuerto por su código IATA de 3 letras (ej. LIM, CUZ)")
    public ResponseEntity<AirportDto> getAirportByIataCode(@PathVariable String iataCode) {
        return ResponseEntity.ok(airportService.getAirportByIataCode(iataCode));
    }
}
