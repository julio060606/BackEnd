package com.chasquifly.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.duffel")
@Getter
@Setter
public class DuffelConfig {

    private boolean enabled = false;
    private String apiToken;
    private String baseUrl = "https://api.duffel.com";
    private String apiVersion = "v2";
    private boolean mockEnabled = false;
    private int connectTimeoutMs = 3000;
    private int readTimeoutMs = 8000;
    private int maxOffers = 10;

    public boolean isUsable() {
        return enabled && apiToken != null && !apiToken.trim().isEmpty();
    }
}
