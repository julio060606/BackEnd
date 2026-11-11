package com.chasquifly.controller;

import com.chasquifly.client.duffel.DuffelService;
import com.chasquifly.client.duffel.dto.DuffelOfferResponse;
import com.chasquifly.client.duffel.dto.DuffelSearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST auxiliar para la búsqueda de ofertas de vuelos en el proveedor externo Duffel.
 * Opera bajo diseño de degradación elegante (Graceful Degradation) y documentación OpenAPI 3 (RNF-05).
 */
@RestController
@RequestMapping("/api/v1/flights/external")
@RequiredArgsConstructor
@Tag(name = "7. Duffel (externo)", description = "Ofertas del proveedor externo Duffel")
public class DuffelController {

    /** Servicio de negocio que encapsula la comunicación con el cliente HTTP de Duffel. */
    private final DuffelService duffelService;

    /**
     * Consulta ofertas externas según origen, destino, fecha de salida y cantidad de pasajeros.
     * Si la integración está deshabilitada o presenta fallos de red, retorna una lista vacía (HTTP 200).
     */
    @GetMapping("/search")
    @Operation(summary = "Buscar ofertas externas en Duffel",
               description = "Lista vacía si Duffel está deshabilitado o si su servicio se cayo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ofertas externas recuperadas de Duffel",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = DuffelOfferResponse.class)))),
        @ApiResponse(responseCode = "400", description = "Parámetros de búsqueda externos inválidos",
            content = @Content(schema = @Schema()))
    })
    public ResponseEntity<List<DuffelOfferResponse>> search(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(defaultValue = "1") Integer passengers) {
        return ResponseEntity.ok(duffelService.searchOffers(
                DuffelSearchRequest.builder()
                        .origin(origin).destination(destination)
                        .departureDate(departureDate).passengers(passengers)
                        .cabinClass("economy").build()));
    }
}
