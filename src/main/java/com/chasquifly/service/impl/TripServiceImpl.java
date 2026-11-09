package com.chasquifly.service.impl;

import com.chasquifly.dto.flight.FlightResponseDto;
import com.chasquifly.dto.trip.CreateTripDto;
import com.chasquifly.dto.trip.TripResponseDto;
import com.chasquifly.exception.BadRequestException;
import com.chasquifly.exception.ResourceNotFoundException;
import com.chasquifly.model.Flight;
import com.chasquifly.model.User;
import com.chasquifly.model.UserTrip;
import com.chasquifly.repository.FlightRepository;
import com.chasquifly.repository.UserRepository;
import com.chasquifly.repository.UserTripRepository;
import com.chasquifly.service.FlightService;
import com.chasquifly.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implementación de la capa de servicio para la gestión de itinerarios personales ({@code user_trips}).
 * Garantiza transaccionalidad en operaciones de lectura/escritura y validación de propiedad por usuario (RF-09).
 */
@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

    private final UserTripRepository userTripRepository;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;
    private final FlightService flightService;

    /**
     * Recupera el itinerario completo del usuario autenticado ordenado por fecha de creación descendente.
     */
    @Override
    @Transactional(readOnly = true)
    public List<TripResponseDto> getUserTrips(UUID userId) {
        return userTripRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::mapToTripDto)
                .toList();
    }

    /**
     * Valida la existencia del usuario y del vuelo antes de persistir el viaje en {@code user_trips}.
     * Lanza {@link ResourceNotFoundException} si alguno de los identificadores no existe en PostgreSQL.
     */
    @Override
    @Transactional
    public TripResponseDto saveTrip(UUID userId, CreateTripDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));

        Flight flight = flightRepository.findById(dto.getFlightId())
                .orElseThrow(() -> new ResourceNotFoundException("Vuelo no encontrado con ID: " + dto.getFlightId()));

        // Construir entidad relacional asociando reserva y notas opcionales del pasajero
        UserTrip trip = UserTrip.builder()
                .user(user)
                .flight(flight)
                .bookingReference(dto.getBookingReference())
                .notes(dto.getNotes())
                .build();

        UserTrip savedTrip = userTripRepository.save(trip);
        return mapToTripDto(savedTrip);
    }

    /**
     * Elimina un viaje guardado verificando previamente la propiedad del usuario autenticado (RNF-02).
     */
    @Override
    @Transactional
    public void deleteTrip(UUID userId, UUID tripId) {
        UserTrip trip = userTripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Viaje no encontrado con ID: " + tripId));

        // Impedir eliminaciones cruzadas entre usuarios distintos (Security by Design)
        if (!trip.getUser().getId().equals(userId)) {
            throw new BadRequestException("No tiene permiso para eliminar este viaje");
        }

        userTripRepository.delete(trip);
    }

    private TripResponseDto mapToTripDto(UserTrip trip) {
        FlightResponseDto flightDto = flightService.getFlightById(trip.getFlight().getId());

        return TripResponseDto.builder()
                .tripId(trip.getId())
                .flight(flightDto)
                .bookingReference(trip.getBookingReference())
                .notes(trip.getNotes())
                .createdAt(trip.getCreatedAt())
                .build();
    }
}
