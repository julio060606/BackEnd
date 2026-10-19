package com.chasquifly.repository;

import com.chasquifly.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio Spring Data JPA para la entidad {@link Airport}.
 * Proporciona métodos de persistencia y consultas personalizadas por código IATA.
 */
@Repository
public interface AirportRepository extends JpaRepository<Airport, Integer> {

    /**
     * Busca un aeropuerto por su código IATA ignorando mayúsculas y minúsculas.
     *
     * @param iataCode Código IATA de 3 letras (ej. "LIM", "cuz").
     * @return Optional con el aeropuerto encontrado o vacío si no existe.
     */
    Optional<Airport> findByIataCodeIgnoreCase(String iataCode);

    /**
     * Verifica la existencia de un aeropuerto por código IATA.
     *
     * @param iataCode Código IATA a validar.
     * @return true si ya existe en la base de datos, false de lo contrario.
     */
    boolean existsByIataCodeIgnoreCase(String iataCode);
}
