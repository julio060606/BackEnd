package com.chasquifly.dto.flight;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightComparisonDto {

    private List<FlightResponseDto> flights;
    private FlightResponseDto cheapestFlight;
    private FlightResponseDto fastestFlight;
    private BigDecimal averagePrice;
    private BigDecimal priceDifference;
}
