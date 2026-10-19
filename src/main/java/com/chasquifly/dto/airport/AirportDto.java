package com.chasquifly.dto.airport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que expone la información pública y de catálogo de un aeropuerto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirportDto {

    /**
     * Identificador del aeropuerto.
     */
    private Integer id;

    /**
     * Código IATA de 3 letras.
     */
    private String iataCode;

    /**
     * Nombre descriptivo de la terminal.
     */
    private String name;

    /**
     * Ciudad sede.
     */
    private String city;

    /**
     * País sede.
     */
    private String country;

    /**
     * Zona horaria IANA del aeropuerto.
     */
    private String timezone;
}
