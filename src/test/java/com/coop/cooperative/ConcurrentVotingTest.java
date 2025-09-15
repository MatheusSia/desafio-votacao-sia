package com.coop.cooperative;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConcurrentVotingTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    @Test
    public void concurrentVoting() throws InterruptedException {
        int threads = 100;
        int total = 1000;
        ExecutorService ex = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(total);
        AtomicInteger success = new AtomicInteger();
        AtomicInteger conflict = new AtomicInteger();

        for (int i = 0; i < total; i++) {
            final long assoc = 10_000_000L + (i % 5000); // some duplicates to test conflicts
            ex.submit(() -> {
                try {
                    Map<String,Object> body = Map.of("associadoId", assoc, "pautaId", 34L, "opcao", "SIM");
                    ResponseEntity<String> r = rest.postForEntity("http://localhost:" + port + "/votos/registrar", body, String.class);
                    if (r.getStatusCode().is2xxSuccessful()) success.incrementAndGet();
                } catch (HttpClientErrorException e) {
                    if (e.getStatusCode() == HttpStatus.CONFLICT || e.getStatusCode() == HttpStatus.BAD_REQUEST) conflict.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        ex.shutdown();
        System.out.println("success: " + success.get() + " conflict: " + conflict.get());
    }
}
