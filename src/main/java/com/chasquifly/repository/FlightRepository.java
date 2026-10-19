package com.chasquifly.repository;

import com.chasquifly.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad Flight con consultas optimizadas de búsqueda y tableros en vivo.
 */
@Repository
public interface FlightRepository extends JpaRepository<Flight, UUID>, JpaSpecificationExecutor<Flight> {

    /**
     * Busca un vuelo por su código de vuelo ignorando mayúsculas y minúsculas.
     *
     * @param flightNumber Código comercial del vuelo (ej. "LA2040").
     * @return Vuelo encontrado o vacío.
     */
    Optional<Flight> findByFlightNumberIgnoreCase(String flightNumber);

    /**
     * Recupera vuelos por estado operativo.
     *
     * @param status Estado operativo del vuelo.
     * @return Lista de vuelos en el estado indicado.
     */
    List<Flight> findByStatus(String status);

    /**
     * Búsqueda multicriterio de vuelos con filtros opcionales de ruta y fecha ordenados por precio ascendente.
     *
     * @param originCode Código IATA del origen (opcional).
     * @param destCode   Código IATA del destino (opcional).
     * @param startDate  Inicio del rango de salida UTC (opcional).
     * @param endDate    Fin del rango de salida UTC (opcional).
     * @return Lista de vuelos coincidentes.
     */
    @Query("SELECT f FROM Flight f " +
           "WHERE (cast(:originCode as string) IS NULL OR UPPER(f.originAirport.iataCode) = UPPER(cast(:originCode as string))) " +
           "AND (cast(:destCode as string) IS NULL OR UPPER(f.destinationAirport.iataCode) = UPPER(cast(:destCode as string))) " +
           "AND (cast(:startDate as timestamp) IS NULL OR f.departureTime >= :startDate) " +
           "AND (cast(:endDate as timestamp) IS NULL OR f.departureTime <= :endDate) " +
           "ORDER BY f.basePrice ASC")
    List<Flight> searchFlights(
            @Param("originCode") String originCode,
            @Param("destCode") String destCode,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate
    );

    /**
     * Consulta vuelos de salida desde un aeropuerto para el tablero FIDS en vivo.
     *
     * @param airportCode Código IATA del aeropuerto de origen.
     * @return Lista de vuelos de salida ordenados por hora de despegue.
     */
    @Query("SELECT f FROM Flight f " +
           "WHERE LOWER(f.originAirport.iataCode) = LOWER(:airportCode) " +
           "ORDER BY f.departureTime ASC")
    List<Flight> findLiveDepartures(@Param("airportCode") String airportCode);

    /**
     * Consulta vuelos de llegada a un aeropuerto para el tablero FIDS en vivo.
     *
     * @param airportCode Código IATA del aeropuerto de destino.
     * @return Lista de vuelos de llegada ordenados por hora de aterrizaje.
     */
    @Query("SELECT f FROM Flight f " +
           "WHERE LOWER(f.destinationAirport.iataCode) = LOWER(:airportCode) " +
           "ORDER BY f.arrivalTime ASC")
    List<Flight> findLiveArrivals(@Param("airportCode") String airportCode);
}
