package com.chasquifly.client.duffel;

import com.chasquifly.client.duffel.dto.DuffelOfferResponse;
import com.chasquifly.client.duffel.dto.DuffelSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Capa de servicio para la búsqueda de ofertas externas mediante el proveedor Duffel.
 * Actúa como fachada sobre {@link DuffelClient} manteniendo el desacoplamiento de capas (SOLID).
 */
@Service
@RequiredArgsConstructor
public class DuffelService {

    /** Cliente HTTP resiliente configurado para comunicarse con Duffel API. */
    private final DuffelClient duffelClient;

    /**
     * Ejecuta la búsqueda de ofertas externas aplicando criterios de ruta, fecha y pasajeros.
     */
    public List<DuffelOfferResponse> searchOffers(DuffelSearchRequest request) {
        return duffelClient.searchOffers(request);
    }

    /**
     * Alias de integración para recuperación de ofertas externas desde servicios internos.
     */
    public List<DuffelOfferResponse> fetchExternalOffers(DuffelSearchRequest request) {
        return duffelClient.searchOffers(request);
    }
}
