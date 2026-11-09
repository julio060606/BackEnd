package com.chasquifly.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Entidad JPA mapeada a la tabla {@code user_trips} en PostgreSQL (RF-09).
 * Representa un vuelo guardado dentro del itinerario personal de un usuario autenticado,
 * incluyendo código de reserva, notas y marca temporal de auditoría.
 */
@Entity
@Table(name = "user_trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTrip {

    /** Identificador primario UUID autogenerado para el registro de itinerario. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Propietario del itinerario; carga perezosa para evitar lecturas innecesarias del perfil. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Vuelo comercial asociado al viaje guardado; disponible de forma inmediata al consultar el itinerario. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    /** Referencia o código de reserva asociado por el pasajero (máximo 50 caracteres). */
    @Column(name = "booking_reference", length = 50)
    private String bookingReference;

    /** Anotaciones libres del usuario sobre equipaje, traslados o recordatorios del viaje. */
    @Column(columnDefinition = "TEXT")
    private String notes;

    /** Marca de auditoría automática registrada al momento de crear el viaje en base de datos. */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
