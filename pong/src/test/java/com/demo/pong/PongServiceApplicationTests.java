package com.demo.pong;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ContextConfiguration(classes = PongServiceApplication.class)
public class PongServiceApplicationTests {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Test
    public void testPongEndpoint() {
        WebClient webClient = webClientBuilder.baseUrl("http://localhost:8081").build();
        Mono<String> response = webClient.get().uri("/pong").retrieve().bodyToMono(String.class);
        assertEquals("World", response.block());
    }

    @Test
    public void testPongEndpointThrottling() {
        WebClient webClient = webClientBuilder.baseUrl("http://localhost:8081").build();

        // First request should succeed
        Mono<String> response1 = webClient.get().uri("/pong").retrieve().bodyToMono(String.class);
        assertEquals("World", response1.block());

        // Second request within the same second should be throttled
        WebClientResponseException exception = assertThrows(WebClientResponseException.class, () -> webClient.get().uri("/pong").retrieve().bodyToMono(String.class).block());

        // Verify that the status code is 429
        assertEquals(429, exception.getStatusCode().value());
    }
}