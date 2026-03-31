package com.coop.cooperative.controller;

import com.coop.cooperative.client.CpfValidationClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cpfs")
public class CpfController {

    private final CpfValidationClient cpfValidationClient;

    public CpfController(CpfValidationClient cpfValidationClient) {
        this.cpfValidationClient = cpfValidationClient;
    }

    @GetMapping("/{cpf}/status")
    public ResponseEntity<?> checkCpf(@PathVariable String cpf) {
        return cpfValidationClient.validateCpf(cpf);
    }
}
