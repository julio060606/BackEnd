package com.chasquifly.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad JPA que representa una aerolínea u operador de vuelos en ChasquiFly.
 * Mapea la tabla {@code airlines}.
 */
@Entity
@Table(name = "airlines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Airline {

    /**
     * Identificador autoincremental de la aerolínea.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Código IATA de 2 letras de la aerolínea (ejemplo: LA, H2, JA).
     */
    @Column(name = "iata_code", nullable = false, unique = true, length = 2)
    private String iataCode;

    /**
     * Razón social o nombre comercial de la aerolínea.
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * URL o ruta relativa hacia el logotipo de la aerolínea.
     */
    @Column(name = "logo_url", length = 255)
    private String logoUrl;
}
