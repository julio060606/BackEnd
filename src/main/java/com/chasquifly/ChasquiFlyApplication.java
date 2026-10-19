package com.chasquifly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.chasquifly.repository")
@EntityScan(basePackages = "com.chasquifly.model")
@EnableScheduling
@EnableAsync
public class ChasquiFlyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChasquiFlyApplication.class, args);
    }
}
