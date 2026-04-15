package com.coop.cooperative.controller;

import com.coop.cooperative.dto.StatusResponse;
import com.coop.cooperative.service.CpfService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cpfs")
public class CpfController {

    private final CpfService cpfService;

    public CpfController(CpfService cpfService) {
        this.cpfService = cpfService;
    }

    @GetMapping("/{cpf}/status")
    public ResponseEntity<?> checkCpf(@PathVariable String cpf) {
        StatusResponse status = cpfService.validarCpf(cpf);
        return ResponseEntity.ok(status);
    }
}
