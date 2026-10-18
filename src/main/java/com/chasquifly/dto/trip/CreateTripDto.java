package com.chasquifly.dto.trip;

import com.chasquifly.dto.flight.FlightResponseDto;
import jakarta.validation.constraints.NotNull;
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
public class CreateTripDto {

    @NotNull(message = "El flightId es obligatorio")
    private UUID flightId;

    private String bookingReference;

    private String notes;
}
