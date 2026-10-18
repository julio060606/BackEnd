package com.chasquifly.client.duffel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DuffelSearchRequest {

    private String origin;
    private String destination;
    private LocalDate departureDate;
    private Integer passengers;
    private String cabinClass;
}
