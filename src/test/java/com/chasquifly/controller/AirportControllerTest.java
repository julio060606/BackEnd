package com.chasquifly.controller;

import com.chasquifly.dto.airport.AirportDto;
import com.chasquifly.exception.GlobalExceptionHandler;
import com.chasquifly.exception.ResourceNotFoundException;
import com.chasquifly.service.AirportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AirportControllerTest {

    @Mock
    private AirportService airportService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AirportController(airportService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void returnsAirportWhenIataCodeExists() throws Exception {
        AirportDto airport = AirportDto.builder().iataCode("LIM").build();
        when(airportService.getAirportByIataCode("LIM")).thenReturn(airport);

        mockMvc.perform(get("/api/v1/airports/LIM"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.iataCode").value("LIM"));
    }

    @Test
    void returnsNotFoundWhenIataCodeDoesNotExist() throws Exception {
        when(airportService.getAirportByIataCode("XXX"))
                .thenThrow(new ResourceNotFoundException("Aeropuerto no encontrado"));

        mockMvc.perform(get("/api/v1/airports/XXX"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/api/v1/airports/XXX"));
    }
}
