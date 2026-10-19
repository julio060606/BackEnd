package com.chasquifly.dto.flight;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * DTO de entrada para las operaciones administrativas de registro y edición de vuelos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFlightDto {

    @NotBlank(message = "El número de vuelo es obligatorio")
    private String flightNumber;

    @NotNull(message = "El ID de la aerolínea es obligatorio")
    private Integer airlineId;

    @NotNull(message = "El ID del aeropuerto de origen es obligatorio")
    private Integer originAirportId;

    @NotNull(message = "El ID del aeropuerto de destino es obligatorio")
    private Integer destinationAirportId;

    @NotNull(message = "La fecha y hora de salida es obligatoria")
    private OffsetDateTime departureTime;

    @NotNull(message = "La fecha y hora de llegada es obligatoria")
    private OffsetDateTime arrivalTime;

    @NotNull(message = "El precio base es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal basePrice;

    @Min(value = 1, message = "Debe haber al menos 1 asiento disponible")
    @Builder.Default
    private Integer availableSeats = 100;

    @Builder.Default
    private Integer stopsCount = 0;

    @NotNull(message = "La duración en minutos es obligatoria")
    @Min(value = 1, message = "La duración debe ser mayor a 0 minutos")
    private Integer durationMinutes;

    @Builder.Default
    private String status = "SCHEDULED";

    private String gate;
    private String terminal;
}
