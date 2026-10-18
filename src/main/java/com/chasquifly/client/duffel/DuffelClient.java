package com.chasquifly.client.duffel;

import com.chasquifly.client.duffel.dto.DuffelOfferResponse;
import com.chasquifly.client.duffel.dto.DuffelSearchRequest;
import com.chasquifly.config.DuffelConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class DuffelClient {

    private final DuffelConfig duffelConfig;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public DuffelClient(DuffelConfig duffelConfig, ObjectMapper objectMapper) {
        this.duffelConfig = duffelConfig;
        this.objectMapper = objectMapper != null ? objectMapper : new ObjectMapper();

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(duffelConfig.getConnectTimeoutMs());
        requestFactory.setReadTimeout(duffelConfig.getReadTimeoutMs());

        this.restClient = RestClient.builder()
                .requestFactory(requestFactory)
                .build();
    }

    public DuffelClient(DuffelConfig duffelConfig) {
        this(duffelConfig, new ObjectMapper());
    }

    public List<DuffelOfferResponse> searchOffers(DuffelSearchRequest request) {
        try {
            // a) Si mockEnabled: devuelve 2 o 3 DuffelOfferResponse falsos pero coherentes
            if (duffelConfig.isMockEnabled()) {
                log.info("Duffel mock is enabled. Returning mock offers.");
                return generateMockOffers(request);
            }

            // b) Si !isUsable(): devuelve lista vacía
            if (!duffelConfig.isUsable()) {
                log.info("Duffel API integration is disabled or token not set. Skipping external flight fetch.");
                return Collections.emptyList();
            }

            // c) Llamada HTTP externa a Duffel API
            return executeDuffelSearch(request);
        } catch (Exception e) {
            // Ante cualquier error registra un warn y devuelve lista vacía. Nunca lanza excepción que cause 500.
            log.warn("Error communicating with Duffel API: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<DuffelOfferResponse> executeDuffelSearch(DuffelSearchRequest request) {
        String baseUrl = (duffelConfig.getBaseUrl() != null && !duffelConfig.getBaseUrl().isBlank())
                ? duffelConfig.getBaseUrl().replaceAll("/+$", "")
                : "https://api.duffel.com";
        String url = baseUrl + "/air/offer_requests?return_offers=true";

        Map<String, Object> slice = new HashMap<>();
        slice.put("origin", request != null && request.getOrigin() != null ? request.getOrigin() : "");
        slice.put("destination", request != null && request.getDestination() != null ? request.getDestination() : "");
        String departureDate = (request != null && request.getDepartureDate() != null)
                ? request.getDepartureDate().toString()
                : LocalDate.now().plusDays(1).toString();
        slice.put("departure_date", departureDate);

        Map<String, String> passenger = Map.of("type", "adult");

        String cabinClass = (request != null && request.getCabinClass() != null && !request.getCabinClass().isBlank())
                ? request.getCabinClass().toLowerCase()
                : "economy";

        Map<String, Object> data = new HashMap<>();
        data.put("slices", List.of(slice));
        data.put("passengers", List.of(passenger));
        data.put("cabin_class", cabinClass);

        Map<String, Object> requestBody = Map.of("data", data);

        log.info("Querying Duffel API for origin: {}, destination: {}", slice.get("origin"), slice.get("destination"));

        JsonNode responseNode = restClient.post()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + duffelConfig.getApiToken())
                .header("Duffel-Version", duffelConfig.getApiVersion())
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .retrieve()
                .body(JsonNode.class);

        if (responseNode == null || !responseNode.has("data")) {
            log.warn("Duffel API returned empty or invalid response body.");
            return Collections.emptyList();
        }

        JsonNode offersNode = responseNode.path("data").path("offers");
        if (!offersNode.isArray() || offersNode.isEmpty()) {
            log.info("No offers found in Duffel response.");
            return Collections.emptyList();
        }

        List<DuffelOfferResponse> offers = new ArrayList<>();
        int limit = duffelConfig.getMaxOffers() > 0 ? duffelConfig.getMaxOffers() : Integer.MAX_VALUE;

        for (JsonNode offerNode : offersNode) {
            if (offers.size() >= limit) {
                break;
            }
            offers.add(mapOfferNodeToResponse(offerNode, request));
        }

        return offers;
    }

    private DuffelOfferResponse mapOfferNodeToResponse(JsonNode offerNode, DuffelSearchRequest request) {
        String id = offerNode.path("id").asText(null);

        JsonNode ownerNode = offerNode.path("owner");
        String airlineCode = ownerNode.path("iata_code").asText("");
        String airlineName = ownerNode.path("name").asText("");

        JsonNode firstSlice = offerNode.path("slices").path(0);
        JsonNode segmentsNode = firstSlice.path("segments");
        JsonNode firstSegment = segmentsNode.path(0);
        JsonNode lastSegment = segmentsNode.size() > 0 ? segmentsNode.path(segmentsNode.size() - 1) : firstSegment;

        String flightNum = firstSegment.path("marketing_carrier_flight_number").asText("");
        if (flightNum.isBlank()) {
            flightNum = firstSegment.path("operating_carrier_flight_number").asText("");
        }
        if (flightNum.isBlank()) {
            flightNum = firstSegment.path("flight_number").asText("");
        }

        if (airlineCode.isBlank()) {
            airlineCode = firstSegment.path("marketing_carrier").path("iata_code").asText("");
        }
        if (airlineName.isBlank()) {
            airlineName = firstSegment.path("marketing_carrier").path("name").asText("");
        }

        String flightNumber = flightNum;
        if (!flightNum.isBlank() && !airlineCode.isBlank() && !flightNum.toUpperCase().startsWith(airlineCode.toUpperCase())) {
            flightNumber = airlineCode + flightNum;
        }

        String origin = firstSlice.path("origin").path("iata_code").asText("");
        if (origin.isBlank()) {
            origin = firstSegment.path("origin").path("iata_code").asText(request != null ? request.getOrigin() : "");
        }

        String destination = firstSlice.path("destination").path("iata_code").asText("");
        if (destination.isBlank()) {
            destination = lastSegment.path("destination").path("iata_code").asText(request != null ? request.getDestination() : "");
        }

        String departureTime = firstSegment.path("departing_at").asText("");
        String arrivalTime = lastSegment.path("arriving_at").asText("");

        String amountStr = offerNode.path("total_amount").asText("0");
        BigDecimal totalAmount;
        try {
            totalAmount = new BigDecimal(amountStr);
        } catch (Exception ex) {
            totalAmount = BigDecimal.ZERO;
        }

        String totalCurrency = offerNode.path("total_currency").asText("USD");

        return DuffelOfferResponse.builder()
                .id(id)
                .airlineCode(airlineCode)
                .airlineName(airlineName)
                .flightNumber(flightNumber)
                .origin(origin)
                .destination(destination)
                .departureTime(departureTime)
                .arrivalTime(arrivalTime)
                .totalAmount(totalAmount)
                .totalCurrency(totalCurrency)
                .build();
    }

    private List<DuffelOfferResponse> generateMockOffers(DuffelSearchRequest request) {
        String origin = (request != null && request.getOrigin() != null && !request.getOrigin().isBlank())
                ? request.getOrigin().toUpperCase()
                : "LIM";
        String destination = (request != null && request.getDestination() != null && !request.getDestination().isBlank())
                ? request.getDestination().toUpperCase()
                : "CUZ";
        String dateStr = (request != null && request.getDepartureDate() != null)
                ? request.getDepartureDate().toString()
                : LocalDate.now().plusDays(7).toString();

        List<DuffelOfferResponse> mocks = List.of(
                DuffelOfferResponse.builder()
                        .id("off_mock_001")
                        .airlineCode("LA")
                        .airlineName("LATAM Airlines")
                        .flightNumber("LA2040")
                        .origin(origin)
                        .destination(destination)
                        .departureTime(dateStr + "T08:00:00")
                        .arrivalTime(dateStr + "T09:20:00")
                        .totalAmount(new BigDecimal("85.50"))
                        .totalCurrency("USD")
                        .build(),
                DuffelOfferResponse.builder()
                        .id("off_mock_002")
                        .airlineCode("H8")
                        .airlineName("SKY Airline")
                        .flightNumber("H85102")
                        .origin(origin)
                        .destination(destination)
                        .departureTime(dateStr + "T13:15:00")
                        .arrivalTime(dateStr + "T14:35:00")
                        .totalAmount(new BigDecimal("72.00"))
                        .totalCurrency("USD")
                        .build(),
                DuffelOfferResponse.builder()
                        .id("off_mock_003")
                        .airlineCode("JA")
                        .airlineName("JetSMART")
                        .flightNumber("JA3210")
                        .origin(origin)
                        .destination(destination)
                        .departureTime(dateStr + "T18:45:00")
                        .arrivalTime(dateStr + "T20:05:00")
                        .totalAmount(new BigDecimal("64.99"))
                        .totalCurrency("USD")
                        .build()
        );

        int limit = duffelConfig.getMaxOffers() > 0 ? duffelConfig.getMaxOffers() : mocks.size();
        return mocks.stream().limit(limit).toList();
    }
}
