package com.chasquifly.repository;

import com.chasquifly.model.UserTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repositorio Spring Data JPA para consultas parametrizadas sobre la tabla {@code user_trips} (RF-09, RNF-02).
 */
@Repository
public interface UserTripRepository extends JpaRepository<UserTrip, UUID> {

    /**
     * Obtiene todos los viajes guardados de un usuario ordenados del más reciente al más antiguo.
     *
     * @param userId UUID del usuario propietario.
     * @return Lista de entidades {@link UserTrip} ordenadas descendentemente por {@code createdAt}.
     */
    List<UserTrip> findByUserIdOrderByCreatedAtDesc(UUID userId);

    /**
     * Lista los itinerarios asociados a un vuelo específico (útil para trazabilidad de eventos del vuelo).
     *
     * @param flightId UUID del vuelo consultado.
     * @return Lista de registros vinculados al vuelo.
     */
    List<UserTrip> findByFlightId(UUID flightId);
}
