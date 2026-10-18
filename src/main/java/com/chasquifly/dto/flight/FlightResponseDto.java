package com.chasquifly.dto.flight;

import com.chasquifly.dto.airport.AirlineDto;
import com.chasquifly.dto.airport.AirportDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightResponseDto {

    private UUID flightId;
    private String flightNumber;
    private AirlineDto airline;
    private AirportDto origin;
    private AirportDto destination;
    private OffsetDateTime departureTime;
    private OffsetDateTime arrivalTime;
    private Integer durationMinutes;
    private Integer stopsCount;
    private BigDecimal basePrice;
    @Builder.Default
    private String currency = "USD";
    private Integer availableSeats;
    private String status;
    private String gate;
    private String terminal;
}
