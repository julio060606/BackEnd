package com.chasquifly.controller;

import com.chasquifly.dto.common.ApiResponse;
import com.chasquifly.dto.trip.CreateTripDto;
import com.chasquifly.dto.trip.TripResponseDto;
import com.chasquifly.security.UserPrincipal;
import com.chasquifly.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

/**
 * Controlador REST para la gestión del itinerario personal ("Mis Viajes") del usuario autenticado.
 * <p>
 * Ruta base: {@code /api/v1/trips} (RF-09).
 * Todos los endpoints exigen autenticación stateless mediante JWT Bearer ({@code BearerAuth}, RNF-02).
 */
@RestController
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
@Tag(name = "5. Mis Viajes", description = "Gestión de vuelos guardados y seguimiento del usuario")
// Esquema OpenAPI BearerAuth aplicado a nivel de controlador para proteger los itinerarios privados
@SecurityRequirement(name = "BearerAuth")
public class TripController {

    /** Servicio de negocio encargado de la persistencia y validación de viajes del usuario. */
    private final TripService tripService;

    /**
     * Consulta los viajes guardados en el itinerario del usuario autenticado (RF-09).
     *
     * @param currentUser Principal extraído del token JWT Bearer en el filtro stateless (RNF-02).
     * @return Lista de viajes asociados al usuario con estado HTTP 200.
     */
    @GetMapping
    @Operation(summary = "Obtener mis viajes", description = "Lista todos los vuelos guardados por el usuario autenticado")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de viajes guardados del usuario",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TripResponseDto.class)))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado — se requiere Bearer Token",
            content = @Content(schema = @Schema()))
    })
    public ResponseEntity<List<TripResponseDto>> getUserTrips(@AuthenticationPrincipal UserPrincipal currentUser) {
        return ResponseEntity.ok(tripService.getUserTrips(currentUser.getId()));
    }

    /**
     * Registra un vuelo seleccionado en el itinerario personal del usuario autenticado (RF-09).
     *
     * @param currentUser Usuario autenticado extraído desde el contexto de seguridad JWT.
     * @param dto         Cuerpo validado con el UUID del vuelo, código de reserva y notas.
     * @return Respuesta envolvente {@link ApiResponse} con estado HTTP 201 Created.
     */
    @PostMapping
    @Operation(summary = "Guardar viaje", description = "Agrega un vuelo al itinerario personal del usuario")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Viaje guardado exitosamente en el itinerario",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos del viaje inválidos",
            content = @Content(schema = @Schema())),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado — se requiere Bearer Token",
            content = @Content(schema = @Schema()))
    })
    public ResponseEntity<ApiResponse<TripResponseDto>> saveTrip(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody CreateTripDto dto) {
        TripResponseDto created = tripService.saveTrip(currentUser.getId(), dto);
        return new ResponseEntity<>(ApiResponse.ok(created, "Viaje guardado exitosamente"), HttpStatus.CREATED);
    }

    /**
     * Elimina un viaje guardado del itinerario del usuario autenticado (RF-09).
     *
     * @param currentUser Principal del usuario dueño de la sesión JWT (RNF-02).
     * @param tripId      UUID del viaje guardado que se desea remover.
     * @return Respuesta de confirmación o error 400/404 si no existe o pertenece a otro usuario.
     */
    @DeleteMapping("/{tripId}")
    @Operation(summary = "Eliminar viaje guardado", description = "Remueve un viaje del itinerario del usuario")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Viaje eliminado exitosamente",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado — se requiere Bearer Token",
            content = @Content(schema = @Schema())),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Viaje no encontrado o no pertenece al usuario",
            content = @Content(schema = @Schema()))
    })
    public ResponseEntity<ApiResponse<Void>> deleteTrip(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable UUID tripId) {
        tripService.deleteTrip(currentUser.getId(), tripId);
        return ResponseEntity.ok(ApiResponse.ok(null, "Viaje eliminado exitosamente"));
    }
}
