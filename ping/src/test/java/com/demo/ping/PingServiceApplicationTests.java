package com.demo.ping;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ContextConfiguration(classes = com.demo.ping.PingServiceApplication.class)
@EnableScheduling
public class PingServiceApplicationTests {

    @Autowired
    private com.demo.ping.PingServiceApplication pingServiceApplication;

    @Test
    public void testSendPing() {
        assertDoesNotThrow(() -> pingServiceApplication.sendPing());
    }
}