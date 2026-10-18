package com.chasquifly.dto.airport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirlineDto {

    private Integer id;
    private String iataCode;
    private String name;
    private String logoUrl;
}
