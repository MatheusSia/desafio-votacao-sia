package com.coop.cooperative.controller;

import com.coop.cooperative.dto.VotoRequest;
import com.coop.cooperative.service.ApiResponseFactory;
import com.coop.cooperative.service.VotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votos")
public class VotoController {

    private final VotoService votoService;
    private final ApiResponseFactory apiResponseFactory;

    public VotoController(VotoService votoService, ApiResponseFactory apiResponseFactory) {
        this.votoService = votoService;
        this.apiResponseFactory = apiResponseFactory;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody VotoRequest request) {
        Long votoId = votoService.registrarVoto(request.getAssociadoId(), request.getPautaId(), request.getOpcao());

        return ResponseEntity.ok(
                apiResponseFactory.formulario("Voto registrado com sucesso", votoId)
        );
    }
}
