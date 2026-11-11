package com.chasquifly.client.duffel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO de respuesta que representa una oferta de vuelo recuperada del proveedor externo Duffel (RNF-05).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Oferta de vuelo comercial proveniente del proveedor externo Duffel")
public class DuffelOfferResponse {

    /** Identificador único de la oferta en Duffel. */
    @Schema(description = "ID único de la oferta en Duffel", example = "off_mock_001")
    private String id;

    /** Código IATA de 2 caracteres de la aerolínea operadora. */
    @Schema(description = "Código IATA de la aerolínea", example = "LA")
    private String airlineCode;

    /** Nombre comercial completo de la aerolínea. */
    @Schema(description = "Nombre comercial de la aerolínea", example = "LATAM Airlines")
    private String airlineName;

    /** Número comercial del vuelo. */
    @Schema(description = "Código comercial del vuelo", example = "LA2040")
    private String flightNumber;

    /** Código IATA de 3 letras del aeropuerto de origen. */
    @Schema(description = "Código IATA del aeropuerto de origen", example = "LIM")
    private String origin;

    /** Código IATA de 3 letras del aeropuerto de destino. */
    @Schema(description = "Código IATA del aeropuerto de destino", example = "CUZ")
    private String destination;

    /** Fecha y hora de salida en formato ISO-8601. */
    @Schema(description = "Fecha y hora programada de salida (ISO-8601)", example = "2026-11-20T08:00:00")
    private String departureTime;

    /** Fecha y hora de llegada en formato ISO-8601. */
    @Schema(description = "Fecha y hora programada de llegada (ISO-8601)", example = "2026-11-20T09:20:00")
    private String arrivalTime;

    /** Monto total de la tarifa expresado con precisión decimal. */
    @Schema(description = "Tarifa total de la oferta", example = "85.50")
    private BigDecimal totalAmount;

    /** Moneda oficial de la oferta (unificada en USD). */
    @Schema(description = "Código ISO de la moneda", example = "USD")
    private String totalCurrency;
}
