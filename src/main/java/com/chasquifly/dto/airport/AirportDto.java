package com.chasquifly.dto.airport;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Datos de catálogo de un aeropuerto")
public class AirportDto {

    @Schema(description = "Identificador numérico del aeropuerto", example = "1")
    private Integer id;

    @Schema(description = "Código IATA de 3 letras", example = "LIM")
    private String iataCode;

    @Schema(description = "Nombre descriptivo de la terminal", example = "Aeropuerto Internacional Jorge Chávez")
    private String name;

    @Schema(description = "Ciudad sede", example = "Lima")
    private String city;

    @Schema(description = "País sede", example = "Perú")
    private String country;

    @Schema(description = "Zona horaria IANA del aeropuerto", example = "America/Lima")
    private String timezone;
}
