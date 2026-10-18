package com.chasquifly.service;

import com.chasquifly.dto.trip.CreateTripDto;
import com.chasquifly.dto.trip.TripResponseDto;

import java.util.List;
import java.util.UUID;

public interface TripService {

    List<TripResponseDto> getUserTrips(UUID userId);

    TripResponseDto saveTrip(UUID userId, CreateTripDto dto);

    void deleteTrip(UUID userId, UUID tripId);
}
