package com.coop.cooperative;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void concurrentVoting() throws Exception {
        long pautaId = criarPauta();
        abrirSessao(pautaId);

        int threads = 100;
        int total = 1000;
        ExecutorService ex = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(total);
        AtomicInteger success = new AtomicInteger();
        AtomicInteger conflict = new AtomicInteger();

        for (int i = 0; i < total; i++) {
            final long assoc = 10_000_000L + (i % 5000);
            ex.submit(() -> {
                try {
                    Map<String,Object> body = Map.of("associadoId", assoc, "pautaId", pautaId, "opcao", "SIM");
                    ResponseEntity<String> r = rest.postForEntity("http://localhost:" + port + "/votos", body, String.class);
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

    private long criarPauta() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = "{\"titulo\":\"Pauta Concorrente\",\"descricao\":\"Teste concorrente\"}";

        ResponseEntity<String> response = rest.postForEntity(
                "http://localhost:" + port + "/pautas",
                new HttpEntity<>(body, headers),
                String.class
        );

        JsonNode json = objectMapper.readTree(response.getBody());
        return json.get("dados").asLong();
    }

    private void abrirSessao(long pautaId) {
        Map<String, Object> body = Map.of("minutos", 1);
        ResponseEntity<String> response = rest.postForEntity(
                "http://localhost:" + port + "/pautas/" + pautaId + "/sessoes",
                body,
                String.class
        );
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("Falha ao abrir sessão para teste concorrente");
        }
    }
}
