package com.chasquifly.dto.flight;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO que encapsula los parámetros y filtros de búsqueda multicriterio de vuelos
 * para el endpoint GET /api/v1/flights/search.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightSearchCriteria {

    private String origin;
    private String destination;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate departureDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate returnDate;

    @Builder.Default
    private Integer passengers = 1;

    @Builder.Default
    private String travelClass = "ECONOMY";

    private BigDecimal maxPrice;
    private Integer airlineId;
    private Integer maxStops;
    private String sortBy; // price_asc, price_desc, duration_asc, departure_asc
}
