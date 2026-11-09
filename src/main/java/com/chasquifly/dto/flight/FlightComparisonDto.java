package com.chasquifly.dto.flight;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO de salida para el comparador analítico de vuelos (RF-06).
 * Centraliza los vuelos evaluados y métricas de decisión económica calculadas con precisión {@link BigDecimal}.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightComparisonDto {

    /** Colección completa de vuelos seleccionados para comparación lado a lado. */
    private List<FlightResponseDto> flights;

    /** Vuelo con la tarifa base más económica del conjunto comparado. */
    private FlightResponseDto cheapestFlight;

    /** Vuelo con la menor duración total de viaje (en minutos). */
    private FlightResponseDto fastestFlight;

    /** Tarifa promedio en USD redondeada a 2 decimales con {@code RoundingMode.HALF_UP}. */
    private BigDecimal averagePrice;

    /** Diferencia neta (ahorro potencial) entre la tarifa más alta y la más económica en USD. */
    private BigDecimal priceDifference;
}
