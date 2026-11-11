package com.chasquifly.client.duffel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO de consulta con los parámetros requeridos para buscar ofertas en el cliente externo Duffel.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Parámetros de entrada para la búsqueda de ofertas externas en Duffel")
public class DuffelSearchRequest {

    /** Código IATA de 3 letras del aeropuerto de origen. */
    @Schema(description = "Código IATA de origen", example = "LIM")
    private String origin;

    /** Código IATA de 3 letras del aeropuerto de destino. */
    @Schema(description = "Código IATA de destino", example = "CUZ")
    private String destination;

    /** Fecha programada de salida del vuelo. */
    @Schema(description = "Fecha de salida del vuelo", example = "2026-11-20")
    private LocalDate departureDate;

    /** Cantidad de pasajeros adultos para la cotización. */
    @Schema(description = "Número de pasajeros", example = "1")
    private Integer passengers;

    /** Clase de cabina solicitada (ej. economy, business). */
    @Schema(description = "Clase de cabina solicitada", example = "economy")
    private String cabinClass;
}
