package com.chasquifly.dto.flight;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

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
