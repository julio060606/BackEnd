package com.chasquifly.repository;

import com.chasquifly.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FlightRepository extends JpaRepository<Flight, UUID>, JpaSpecificationExecutor<Flight> {

    Optional<Flight> findByFlightNumberIgnoreCase(String flightNumber);

    List<Flight> findByStatus(String status);

    @Query("SELECT f FROM Flight f " +
           "WHERE (:originCode IS NULL OR LOWER(f.originAirport.iataCode) = LOWER(:originCode)) " +
           "AND (:destCode IS NULL OR LOWER(f.destinationAirport.iataCode) = LOWER(:destCode)) " +
           "AND (cast(:startDate as timestamp) IS NULL OR f.departureTime >= :startDate) " +
           "AND (cast(:endDate as timestamp) IS NULL OR f.departureTime <= :endDate) " +
           "ORDER BY f.basePrice ASC")
    List<Flight> searchFlights(
            @Param("originCode") String originCode,
            @Param("destCode") String destCode,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate
    );

    @Query("SELECT f FROM Flight f " +
           "WHERE LOWER(f.originAirport.iataCode) = LOWER(:airportCode) " +
           "ORDER BY f.departureTime ASC")
    List<Flight> findLiveDepartures(@Param("airportCode") String airportCode);

    @Query("SELECT f FROM Flight f " +
           "WHERE LOWER(f.destinationAirport.iataCode) = LOWER(:airportCode) " +
           "ORDER BY f.arrivalTime ASC")
    List<Flight> findLiveArrivals(@Param("airportCode") String airportCode);
}
