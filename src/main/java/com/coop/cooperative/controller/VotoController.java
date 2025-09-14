package com.coop.cooperative.controller;

import com.coop.cooperative.dto.ResultadoResponse;
import com.coop.cooperative.dto.VotoRequest;
import com.coop.cooperative.entity.Voto;
import com.coop.cooperative.service.VotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votos")
public class VotoController {

    private final VotoService votoService;

    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody VotoRequest request) {
        // o service lança BusinessException / ResourceNotFoundException quando necessário
        Voto voto = votoService.registrarVoto(request.getAssociadoId(), request.getPautaId(), request.getOpcao());

        return ResponseEntity.ok(
                new ResultadoResponse("FORMULARIO", "Voto registrado com sucesso", voto.getId())
        );
    }
}
