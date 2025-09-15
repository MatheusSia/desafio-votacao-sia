package com.coop.cooperative.client;

import com.coop.cooperative.dto.StatusResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CpfValidationClient {

    private final Random random = new Random();

    public ResponseEntity<?> validateCpf(String cpf) {
        // 30% de chance de o CPF ser inválido
        if (random.nextInt(10) < 3) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CPF inválido");
        }

        // Se válido, decide aleatoriamente se pode votar
        boolean ableToVote = random.nextBoolean();
        if (ableToVote) {
            return ResponseEntity.ok(new StatusResponse("ABLE_TO_VOTE"));
        } else {
            return ResponseEntity.ok(new StatusResponse("UNABLE_TO_VOTE"));
        }
    }
}
