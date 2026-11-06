package com.chasquifly.dto.trip;

import com.chasquifly.dto.flight.FlightResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detalle de un viaje guardado en el itinerario del usuario")
public class TripResponseDto {

    @Schema(description = "Identificador único del viaje guardado", example = "6fa459ea-ee8a-3ca4-894e-db77e160355e")
    private UUID tripId;

    @Schema(description = "Datos completos del vuelo asociado")
    private FlightResponseDto flight;

    @Schema(description = "Código de reserva o referencia interna", example = "CHASQ-84920")
    private String bookingReference;

    @Schema(description = "Notas personalizadas del usuario", example = "Viaje familiar de vacaciones")
    private String notes;

    @Schema(description = "Fecha de registro del viaje")
    private OffsetDateTime createdAt;
}
