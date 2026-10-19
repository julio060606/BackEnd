package com.chasquifly.repository;

import com.chasquifly.model.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio Spring Data JPA para la entidad {@link Airline}.
 * Permite la gestión y búsqueda de aerolíneas en la base de datos.
 */
@Repository
public interface AirlineRepository extends JpaRepository<Airline, Integer> {

    /**
     * Busca una aerolínea por su código IATA ignorando mayúsculas y minúsculas.
     *
     * @param iataCode Código de 2 caracteres (ej. "LA", "H2").
     * @return Optional con la entidad encontrada.
     */
    Optional<Airline> findByIataCodeIgnoreCase(String iataCode);
}
