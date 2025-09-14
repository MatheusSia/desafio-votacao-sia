package com.coop.cooperative.controller;

import com.coop.cooperative.dto.AbrirSessaoRequest;
import com.coop.cooperative.dto.CriarPautaRequest;
import com.coop.cooperative.dto.ResultadoResponse;
import com.coop.cooperative.dto.ResultadoVotacao;
import com.coop.cooperative.entity.Pauta;
import com.coop.cooperative.entity.SessaoVotacao;
import com.coop.cooperative.service.PautaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pautas")
public class PautaController {

    private final PautaService pautaService;

    public PautaController(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody CriarPautaRequest request) {
        Pauta pauta = pautaService.criarPauta(request.getTitulo(), request.getDescricao());
        return ResponseEntity.ok(
                new ResultadoResponse("FORMULARIO", "Pauta cadastrada com sucesso", pauta.getId())
        );
    }

    @PostMapping("/{id}/abrir-sessao")
    public ResponseEntity<?> abrirSessao(
            @PathVariable Long id,
            @RequestBody(required = false) AbrirSessaoRequest request) {

        Integer minutos = request == null ? null : request.getMinutos();
        SessaoVotacao sessao = pautaService.abrirSessao(id, minutos);

        return ResponseEntity.ok(
                new ResultadoResponse("FORMULARIO",
                        "Sessão aberta por " + (minutos == null ? 1 : minutos) + " minuto(s)",
                        sessao.getId())
        );
    }

    @GetMapping("/{id}/resultado")
    public ResponseEntity<?> resultado(@PathVariable Long id) {
        ResultadoVotacao resultado = pautaService.obterResultado(id);

        return ResponseEntity.ok(
                new ResultadoResponse("SELECAO", "Resultado da votação", resultado)
        );
    }
}
