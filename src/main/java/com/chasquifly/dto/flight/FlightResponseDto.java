package com.chasquifly.dto.flight;

import com.chasquifly.dto.airport.AirlineDto;
import com.chasquifly.dto.airport.AirportDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO de respuesta que representa los detalles completos de un vuelo comercial,
 * incluyendo aerolínea operadora, aeropuertos de origen/destino y tarifas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detalles completos de un vuelo indexado en ChasquiFly")
public class FlightResponseDto {

    @Schema(description = "Identificador único del vuelo", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID flightId;

    @Schema(description = "Código de vuelo IATA", example = "LA2040")
    private String flightNumber;

    @Schema(description = "Aerolínea operadora del vuelo")
    private AirlineDto airline;

    @Schema(description = "Aeropuerto de origen")
    private AirportDto origin;

    @Schema(description = "Aeropuerto de destino")
    private AirportDto destination;

    @Schema(description = "Fecha y hora de salida en formato ISO-8601 con zona horaria", example = "2024-12-15T08:30:00-05:00")
    private OffsetDateTime departureTime;

    @Schema(description = "Fecha y hora de llegada en formato ISO-8601 con zona horaria", example = "2024-12-15T10:45:00-05:00")
    private OffsetDateTime arrivalTime;

    @Schema(description = "Duración total del vuelo en minutos", example = "135")
    private Integer durationMinutes;

    /** Cero escalas significa que el vuelo es directo. */
    @Schema(description = "Número de escalas (0 = directo)", example = "0")
    private Integer stopsCount;

    /** La tarifa base del vuelo se expresa en dólares estadounidenses. */
    @Schema(description = "Precio base del vuelo en USD", example = "59.99")
    private BigDecimal basePrice;

    @Schema(description = "Moneda del precio", example = "USD")
    @Builder.Default
    private String currency = "USD";

    /** Cantidad de plazas que siguen disponibles para reservar. */
    @Schema(description = "Asientos disponibles", example = "42")
    private Integer availableSeats;

    @Schema(description = "Estado operativo del vuelo", example = "ON_TIME", allowableValues = {"ON_TIME", "DELAYED", "CANCELLED", "BOARDING"})
    private String status;

    @Schema(description = "Puerta de embarque", example = "B12")
    private String gate;

    @Schema(description = "Terminal del aeropuerto", example = "T2")
    private String terminal;
}
