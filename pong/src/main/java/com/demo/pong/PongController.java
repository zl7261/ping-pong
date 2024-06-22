package com.demo.pong;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
public class PongController {
    private final RateLimiter rateLimiter;

    public PongController(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }


    @GetMapping("/pong")
    public Mono<String> pong() {
        if (rateLimiter.tryAcquire()) {
            return Mono.just("World");
        }

        return Mono.error(new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests"));
    }
}
