package com.chasquifly.client.duffel;

import com.chasquifly.client.duffel.dto.DuffelOfferResponse;
import com.chasquifly.client.duffel.dto.DuffelSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DuffelService {

    private final DuffelClient duffelClient;

    public List<DuffelOfferResponse> fetchExternalOffers(DuffelSearchRequest request) {
        return duffelClient.searchOffers(request);
    }
}
