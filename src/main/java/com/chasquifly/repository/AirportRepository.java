package com.chasquifly.repository;

import com.chasquifly.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Integer> {

    Optional<Airport> findByIataCodeIgnoreCase(String iataCode);

    boolean existsByIataCodeIgnoreCase(String iataCode);
}
