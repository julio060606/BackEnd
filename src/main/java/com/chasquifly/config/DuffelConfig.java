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
}
