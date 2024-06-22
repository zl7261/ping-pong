package com.demo.pong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;



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
        } else {
            return Mono.delay(Duration.ofSeconds(1))
                    .then(Mono.error(new TooManyRequestsException()));
        }
    }
}
