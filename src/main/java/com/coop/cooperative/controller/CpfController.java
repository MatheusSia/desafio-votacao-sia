package com.coop.cooperative.controller;

import com.coop.cooperative.dto.StatusResponse;
import com.coop.cooperative.service.CpfService;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/cpfs")
public class CpfController {

    private final CpfService cpfService;

    public CpfController(CpfService cpfService) {
        this.cpfService = cpfService;
    }

    @GetMapping("/{cpf}/status")
    public ResponseEntity<?> checkCpf(
            @PathVariable
            @Pattern(regexp = "\\d{11}", message = "cpf deve conter exatamente 11 dígitos numéricos")
            String cpf) {
        StatusResponse status = cpfService.validarCpf(cpf);
        return ResponseEntity.ok(status);
    }
}
