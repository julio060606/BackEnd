package com.chasquifly.dto.airport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa los datos expuestos de una aerolínea.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirlineDto {

    /**
     * Identificador de la aerolínea.
     */
    private Integer id;

    /**
     * Código IATA de 2 caracteres.
     */
    private String iataCode;

    /**
     * Nombre comercial de la aerolínea.
     */
    private String name;

    /**
     * URL o ruta de acceso al logo corporativo.
     */
    private String logoUrl;
}
