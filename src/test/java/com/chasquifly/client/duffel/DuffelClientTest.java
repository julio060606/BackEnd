package com.chasquifly.client.duffel;

import com.chasquifly.client.duffel.dto.DuffelOfferResponse;
import com.chasquifly.client.duffel.dto.DuffelSearchRequest;
import com.chasquifly.config.DuffelConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de pruebas unitarias para la integración externa con Duffel (RNF-06).
 * Certifica las 7 pruebas de validación de credenciales, modo mock, límites y degradación elegante.
 */
class DuffelClientTest {

    private DuffelConfig config;
    private DuffelClient client;

    @BeforeEach
    void setUp() {
        config = new DuffelConfig();
        client = new DuffelClient(config);
    }

    /** Prueba 1/7 (RNF-06): Certifica que isUsable() exija habilitación explícita y token no vacío. */
    @Test
    @DisplayName("DuffelConfig: isUsable() retorna true solo cuando enabled=true y apiToken no está vacío")
    void testDuffelConfigIsUsable() {
        assertFalse(config.isUsable(), "Por defecto no debe ser utilizable");

        config.setEnabled(true);
        assertFalse(config.isUsable(), "Enabled sin token no debe ser utilizable");

        config.setApiToken("");
        assertFalse(config.isUsable(), "Enabled con token vacío no debe ser utilizable");

        config.setApiToken("   ");
        assertFalse(config.isUsable(), "Enabled con token en blanco no debe ser utilizable");

        config.setApiToken("duffel_test_12345");
        assertTrue(config.isUsable(), "Enabled con token válido debe ser utilizable");

        config.setEnabled(false);
        assertFalse(config.isUsable(), "Disabled con token no debe ser utilizable");
    }

    /** Prueba 2/7 (RNF-06): Certifica la generación de ofertas simuladas coherentes en USD cuando mockEnabled=true. */
    @Test
    @DisplayName("DuffelClient: Cuando mockEnabled=true, retorna ofertas simuladas coherentes con la solicitud")
    void testSearchOffersWhenMockEnabledReturnsMockOffers() {
        config.setMockEnabled(true);
        LocalDate targetDate = LocalDate.now().plusDays(10);

        DuffelSearchRequest request = DuffelSearchRequest.builder()
                .origin("AQP")
                .destination("LIM")
                .departureDate(targetDate)
                .passengers(1)
                .cabinClass("economy")
                .build();

        List<DuffelOfferResponse> offers = client.searchOffers(request);

        assertThat(offers).isNotEmpty().hasSize(3);

        DuffelOfferResponse first = offers.get(0);
        assertThat(first.getId()).isEqualTo("off_mock_001");
        assertThat(first.getAirlineCode()).isEqualTo("LA");
        assertThat(first.getFlightNumber()).isEqualTo("LA2040");
        assertThat(first.getOrigin()).isEqualTo("AQP");
        assertThat(first.getDestination()).isEqualTo("LIM");
        assertThat(first.getDepartureTime()).startsWith(targetDate.toString());
        assertThat(first.getTotalAmount()).isGreaterThan(BigDecimal.ZERO);
        assertThat(first.getTotalCurrency()).isEqualTo("USD");

        DuffelOfferResponse second = offers.get(1);
        assertThat(second.getAirlineCode()).isEqualTo("H8");
        assertThat(second.getOrigin()).isEqualTo("AQP");
        assertThat(second.getDestination()).isEqualTo("LIM");

        DuffelOfferResponse third = offers.get(2);
        assertThat(third.getAirlineCode()).isEqualTo("JA");
        assertThat(third.getOrigin()).isEqualTo("AQP");
        assertThat(third.getDestination()).isEqualTo("LIM");
    }

    /** Prueba 3/7 (RNF-06): Certifica que el listado de ofertas respete la cota máxima maxOffers. */
    @Test
    @DisplayName("DuffelClient: mockEnabled respeta el límite maxOffers")
    void testSearchOffersWhenMockEnabledRespectsMaxOffers() {
        config.setMockEnabled(true);
        config.setMaxOffers(2);

        DuffelSearchRequest request = DuffelSearchRequest.builder()
                .origin("LIM")
                .destination("CUZ")
                .departureDate(LocalDate.now().plusDays(3))
                .build();

        List<DuffelOfferResponse> offers = client.searchOffers(request);

        assertThat(offers).hasSize(2);
    }

    /** Prueba 4/7 (RNF-06): Certifica retorno de lista vacía sin llamadas de red cuando la integración está inactiva. */
    @Test
    @DisplayName("DuffelClient: Cuando !isUsable() (disabled o sin token), retorna lista vacía sin llamadas HTTP")
    void testSearchOffersWhenNotUsableReturnsEmptyList() {
        config.setMockEnabled(false);
        config.setEnabled(false);

        DuffelSearchRequest request = DuffelSearchRequest.builder()
                .origin("LIM")
                .destination("CUZ")
                .departureDate(LocalDate.now().plusDays(5))
                .build();

        List<DuffelOfferResponse> offers = client.searchOffers(request);
        assertThat(offers).isEmpty();

        // Con enabled=true pero sin token
        config.setEnabled(true);
        config.setApiToken(null);
        List<DuffelOfferResponse> offersNoToken = client.searchOffers(request);
        assertThat(offersNoToken).isEmpty();
    }

    /** Prueba 5/7 (RNF-06): Certifica prevención de NullPointerException ante parámetros de búsqueda nulos. */
    @Test
    @DisplayName("DuffelClient: Maneja solicitudes nulas de forma segura sin lanzar NullPointerException")
    void testSearchOffersWithNullRequest() {
        config.setMockEnabled(true);
        List<DuffelOfferResponse> offers = client.searchOffers(null);

        assertThat(offers).isNotEmpty().hasSize(3);
        assertThat(offers.get(0).getOrigin()).isEqualTo("LIM");
        assertThat(offers.get(0).getDestination()).isEqualTo("CUZ");
    }

    /** Prueba 6/7 (RNF-06): Certifica degradación elegante ante caídas o timeouts sin arrojar HTTP 500. */
    @Test
    @DisplayName("DuffelClient: Ante fallos de conexión externa, captura el error y retorna lista vacía sin 500")
    void testSearchOffersGracefulDegradationOnFailure() {
        config.setEnabled(true);
        config.setMockEnabled(false);
        config.setApiToken("fake_token_for_resilience_test");
        config.setBaseUrl("http://localhost:/8080"); 
        config.setConnectTimeoutMs(100);
        config.setReadTimeoutMs(100);

        DuffelClient resilientClient = new DuffelClient(config);

        DuffelSearchRequest request = DuffelSearchRequest.builder()
                .origin("LIM")
                .destination("CUZ")
                .departureDate(LocalDate.now().plusDays(2))
                .build();

        assertDoesNotThrow(() -> {
            List<DuffelOfferResponse> offers = resilientClient.searchOffers(request);
            assertThat(offers).isEmpty();
        });
    }

    /** Prueba 7/7 (RNF-06): Certifica la delegación de llamadas desde DuffelService hacia DuffelClient. */
    @Test
    @DisplayName("DuffelService: Delega correctamente en DuffelClient")
    void testDuffelServiceDelegation() {
        config.setMockEnabled(true);
        DuffelService service = new DuffelService(client);

        DuffelSearchRequest request = DuffelSearchRequest.builder()
                .origin("LIM")
                .destination("CUZ")
                .departureDate(LocalDate.now().plusDays(4))
                .build();

        List<DuffelOfferResponse> offers = service.searchOffers(request);
        assertThat(offers).hasSize(3);

        List<DuffelOfferResponse> externalOffers = service.fetchExternalOffers(request);
        assertThat(externalOffers).hasSize(3);
    }
}

