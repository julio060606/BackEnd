package com.chasquifly.client.duffel;

import com.chasquifly.client.duffel.dto.DuffelOfferResponse;
import com.chasquifly.client.duffel.dto.DuffelSearchRequest;
import com.chasquifly.config.DuffelConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DuffelClient {

    private final DuffelConfig duffelConfig;

    public List<DuffelOfferResponse> searchOffers(DuffelSearchRequest request) {
        if (!duffelConfig.isEnabled() || duffelConfig.getApiToken() == null || duffelConfig.getApiToken().isBlank()) {
            log.info("Duffel API integration is disabled or token not set. Skipping external flight fetch.");
            return Collections.emptyList();
        }

        try {
            log.info("Querying Duffel API for origin: {}, destination: {}", request.getOrigin(), request.getDestination());
            // Hook for Duffel SDK / HTTP RestClient calls
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("Error communicating with Duffel API: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
