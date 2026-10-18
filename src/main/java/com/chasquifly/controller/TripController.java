package com.chasquifly.controller;

import com.chasquifly.dto.common.ApiResponse;
import com.chasquifly.dto.trip.CreateTripDto;
import com.chasquifly.dto.trip.TripResponseDto;
import com.chasquifly.security.UserPrincipal;
import com.chasquifly.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
@Tag(name = "5. Mis Viajes", description = "Gestión de vuelos guardados y seguimiento del usuario")
@SecurityRequirement(name = "BearerAuth")
public class TripController {

    private final TripService tripService;

    @GetMapping
    @Operation(summary = "Obtener mis viajes", description = "Lista todos los vuelos guardados por el usuario autenticado")
    public ResponseEntity<List<TripResponseDto>> getUserTrips(@AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(tripService.getUserTrips(currentUser.getId()));
    }

    @PostMapping
    @Operation(summary = "Guardar viaje", description = "Agrega un vuelo al itinerario personal del usuario")
    public ResponseEntity<ApiResponse<TripResponseDto>> saveTrip(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody CreateTripDto dto) {
        TripResponseDto created = tripService.saveTrip(currentUser.getId(), dto);
        return new ResponseEntity<>(ApiResponse.ok(created, "Viaje guardado exitosamente"), HttpStatus.CREATED);
    }

    @DeleteMapping("/{tripId}")
    @Operation(summary = "Eliminar viaje guardado", description = "Remueve un viaje del itinerario del usuario")
    public ResponseEntity<ApiResponse<Void>> deleteTrip(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable UUID tripId) {
        tripService.deleteTrip(currentUser.getId(), tripId);
        return ResponseEntity.ok(ApiResponse.ok(null, "Viaje eliminado exitosamente"));
    }
}
