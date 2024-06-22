package com.demo.ping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@EnableScheduling
public class PingServiceApplication {

    private final WebClient webClient = WebClient.create("http://localhost:8081");
    private static final String LOCK_FILE = "rate_limit.lock";
    private static final int MAX_REQUESTS_PER_SECOND = 2 * 1000;

    public static void main(String[] args) {
        SpringApplication.run(PingServiceApplication.class, args);
    }

    @Scheduled(fixedRate = MAX_REQUESTS_PER_SECOND)
    public void sendPing() {
        try (RandomAccessFile raf = new RandomAccessFile(new File(LOCK_FILE), "rw");
             FileChannel channel = raf.getChannel();
             FileLock lock = channel.tryLock()) {

            if (lock != null) {
                webClient.get()
                        .uri("/pong")
                        .retrieve()
                        .bodyToMono(String.class)
                        .doOnNext(response -> logResult("Request sent & Pong Respond: " + response))
                        .doOnError(error -> logResult("Request send & Pong throttled it: " + error.getMessage()))
                        .subscribe();
            } else {
                logResult("Request not sent as being rate limited");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logResult(String result) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        System.out.println(timestamp + " - " + result);
    }
}