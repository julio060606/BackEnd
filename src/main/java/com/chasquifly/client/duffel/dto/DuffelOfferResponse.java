package com.chasquifly.client.duffel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DuffelOfferResponse {

    private String id;
    private String airlineCode;
    private String airlineName;
    private String flightNumber;
    private String origin;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private BigDecimal totalAmount;
    private String totalCurrency;
}
