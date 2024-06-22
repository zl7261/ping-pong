package com.demo.pong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PongServiceApplication {
    @Bean
    public RateLimiter rateLimiter() {
        return new RateLimiter(1); // Limit to 1 request per second
    }
    public static void main(String[] args) {
        SpringApplication.run(PongServiceApplication.class, args);
    }
}



