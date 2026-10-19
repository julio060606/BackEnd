package com.chasquifly.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad JPA que representa un aeropuerto en la plataforma ChasquiFly.
 * Mapea la tabla {@code airports} administrada mediante migraciones de Flyway.
 */
@Entity
@Table(name = "airports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Airport {

    /**
     * Identificador único autoincremental del aeropuerto.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Código oficial IATA de 3 caracteres (ejemplo: LIM, CUZ, AQP).
     */
    @Column(name = "iata_code", nullable = false, unique = true, length = 3)
    private String iataCode;

    /**
     * Nombre comercial o institucional completo del aeropuerto.
     */
    @Column(nullable = false, length = 150)
    private String name;

    /**
     * Ciudad donde se ubica el aeropuerto físico.
     */
    @Column(nullable = false, length = 100)
    private String city;

    /**
     * País de radicación de la terminal aérea.
     */
    @Column(nullable = false, length = 100)
    private String country;

    /**
     * Zona horaria IANA para la programación precisa de vuelos.
     * Valor por defecto: America/Lima.
     */
    @Column(nullable = false, length = 50)
    @Builder.Default
    private String timezone = "America/Lima";
}
