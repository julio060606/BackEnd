package com.chasquifly.dto.trip;

import com.chasquifly.dto.flight.FlightResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripResponseDto {

    private UUID tripId;
    private FlightResponseDto flight;
    private String bookingReference;
    private String notes;
    private OffsetDateTime createdAt;
}
