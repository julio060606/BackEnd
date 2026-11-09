package com.chasquifly.service;

import com.chasquifly.dto.trip.CreateTripDto;
import com.chasquifly.dto.trip.TripResponseDto;

import java.util.List;
import java.util.UUID;

/**
 * Contrato de la capa de servicio para la gestión de itinerarios personales (RF-09).
 * Define las operaciones autenticadas de consulta, registro y eliminación sobre viajes guardados.
 */
public interface TripService {

    /**
     * Lista los viajes guardados del usuario ordenados cronológicamente de más reciente a más antiguo.
     *
     * @param userId Identificador único del usuario autenticado.
     * @return Lista de DTOs {@link TripResponseDto} pertenecientes al usuario.
     */
    List<TripResponseDto> getUserTrips(UUID userId);

    /**
     * Registra un vuelo seleccionado en el itinerario del usuario autenticado.
     *
     * @param userId Identificador único del usuario autenticado.
     * @param dto    Datos de entrada con el vuelo, referencia de reserva y notas.
     * @return DTO del viaje guardado con información completa del vuelo.
     */
    TripResponseDto saveTrip(UUID userId, CreateTripDto dto);

    /**
     * Elimina un viaje del itinerario verificando previamente que pertenezca al usuario solicitante.
     *
     * @param userId Identificador único del usuario autenticado.
     * @param tripId Identificador único del viaje guardado a eliminar.
     */
    void deleteTrip(UUID userId, UUID tripId);
}
