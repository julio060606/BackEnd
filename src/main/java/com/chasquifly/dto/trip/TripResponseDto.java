package com.chasquifly.dto.trip;

import com.chasquifly.dto.flight.FlightResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO de respuesta que expone el detalle de un viaje guardado en el itinerario del usuario (RF-09).
 * Integra el modelo de vuelo completo junto con los metadatos personales de reserva bajo OpenAPI 3 (RNF-05).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detalle de un viaje guardado en el itinerario del usuario")
public class TripResponseDto {

    /** Identificador único del registro en el itinerario personal. */
    @Schema(description = "Identificador único del viaje guardado", example = "6fa459ea-ee8a-3ca4-894e-db77e160355e")
    private UUID tripId;

    /** Representación completa del vuelo comercial asociado (aerolínea, aeropuertos, horarios y tarifa). */
    @Schema(description = "Datos completos del vuelo asociado")
    private FlightResponseDto flight;

    @Schema(description = "Código de reserva o referencia interna", example = "CHASQ-84920")
    private String bookingReference;

    @Schema(description = "Notas personalizadas del usuario", example = "Viaje familiar de vacaciones")
    private String notes;

    @Schema(description = "Fecha de registro del viaje")
    private OffsetDateTime createdAt;
}
