package com.chasquifly.repository;

import com.chasquifly.model.UserTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserTripRepository extends JpaRepository<UserTrip, UUID> {

    List<UserTrip> findByUserIdOrderByCreatedAtDesc(UUID userId);

    List<UserTrip> findByFlightId(UUID flightId);
}
