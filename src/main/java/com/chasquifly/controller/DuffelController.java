package com.chasquifly.controller;

import com.chasquifly.client.duffel.DuffelService;
import com.chasquifly.client.duffel.dto.DuffelOfferResponse;
import com.chasquifly.client.duffel.dto.DuffelSearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/flights/external")
@RequiredArgsConstructor
@Tag(name = "7. Duffel (externo)", description = "Ofertas del proveedor externo Duffel")
public class DuffelController {

    private final DuffelService duffelService;

    @GetMapping("/search")
    @Operation(summary = "Buscar ofertas externas en Duffel",
               description = "Lista vacía si Duffel está deshabilitado o si su servicio se cayo")
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