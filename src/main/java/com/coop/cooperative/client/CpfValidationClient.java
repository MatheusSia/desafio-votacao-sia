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
        if (!isValidCpf(cpf)) {
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

    // Validação real de CPF
    private boolean isValidCpf(String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }
            int checkDigit1 = 11 - (sum % 11);
            checkDigit1 = (checkDigit1 >= 10) ? 0 : checkDigit1;

            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }
            int checkDigit2 = 11 - (sum % 11);
            checkDigit2 = (checkDigit2 >= 10) ? 0 : checkDigit2;

            return checkDigit1 == Character.getNumericValue(cpf.charAt(9))
                    && checkDigit2 == Character.getNumericValue(cpf.charAt(10));
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
