package com.demo.pong;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RateLimiter {
    private final int maxRequestsPerSecond;
    private final AtomicInteger requestCount;
    private final AtomicLong lastResetTime;

    public RateLimiter(int maxRequestsPerSecond) {
        this.maxRequestsPerSecond = maxRequestsPerSecond;
        this.requestCount = new AtomicInteger(0);
        this.lastResetTime = new AtomicLong(System.currentTimeMillis());
    }

    public boolean tryAcquire() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastResetTime.get();

        if (elapsedTime > 1000) {
            requestCount.set(0);
            lastResetTime.set(currentTime);
        }

        return requestCount.incrementAndGet() <= maxRequestsPerSecond;
    }
}