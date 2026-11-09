package com.chasquifly.dto.trip;

import com.chasquifly.dto.flight.FlightResponseDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO de entrada para registrar un vuelo dentro del itinerario personal del usuario (RF-09).
 * Valida la presencia obligatoria del identificador del vuelo y admite referencia de reserva y notas opcionales.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTripDto {

    /** UUID del vuelo indexado en PostgreSQL que se desea vincular al itinerario. */
    @NotNull(message = "El flightId es obligatorio")
    private UUID flightId;

    /** Código de reserva o referencia externa opcional proporcionada por el pasajero. */
    private String bookingReference;

    /** Notas o comentarios personales sobre el viaje (equipaje, acompañantes o recordatorios). */
    private String notes;
}
