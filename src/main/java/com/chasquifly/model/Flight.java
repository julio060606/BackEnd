package com.chasquifly.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Entidad JPA que representa un vuelo indexado con su tarifa base, horarios y estado operativo.
 */
@Entity
@Table(name = "flights")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "flight_number", nullable = false, length = 20)
    private String flightNumber;

    /** Aerolínea operadora del vuelo (relación N:1). */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airline;

    /** Aeropuerto de salida (relación N:1). */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "origin_airport_id", nullable = false)
    private Airport originAirport;

    /** Aeropuerto de llegada (relación N:1). */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_airport_id", nullable = false)
    private Airport destinationAirport;

    @Column(name = "departure_time", nullable = false)
    private OffsetDateTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private OffsetDateTime arrivalTime;

    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "available_seats", nullable = false)
    @Builder.Default
    private Integer availableSeats = 100;

    @Column(name = "stops_count", nullable = false)
    @Builder.Default
    private Integer stopsCount = 0;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "SCHEDULED";

    @Column(length = 20)
    private String gate;

    @Column(length = 10)
    private String terminal;
}
