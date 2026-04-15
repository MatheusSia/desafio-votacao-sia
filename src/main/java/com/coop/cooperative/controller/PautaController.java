package com.coop.cooperative.controller;

import com.coop.cooperative.dto.AbrirSessaoRequest;
import com.coop.cooperative.dto.CriarPautaRequest;
import com.coop.cooperative.dto.ResultadoVotacao;
import com.coop.cooperative.dto.SessaoAberturaResponse;
import com.coop.cooperative.entity.Pauta;
import com.coop.cooperative.service.ApiResponseFactory;
import com.coop.cooperative.service.PautaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pautas")
public class PautaController {

    private final PautaService pautaService;
    private final ApiResponseFactory apiResponseFactory;

    public PautaController(PautaService pautaService, ApiResponseFactory apiResponseFactory) {
        this.pautaService = pautaService;
        this.apiResponseFactory = apiResponseFactory;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody CriarPautaRequest request) {
        Pauta pauta = pautaService.criarPauta(request.getTitulo(), request.getDescricao());
        return ResponseEntity.ok(
                apiResponseFactory.formulario("Pauta cadastrada com sucesso", pauta.getId())
        );
    }

    @PostMapping("/{id}/sessoes")
    public ResponseEntity<?> abrirSessao(
            @PathVariable Long id,
            @RequestBody(required = false) AbrirSessaoRequest request) {

        Integer minutos = request == null ? null : request.getMinutos();
        SessaoAberturaResponse sessao = pautaService.abrirSessaoComResumo(id, minutos);

        return ResponseEntity.ok(
                apiResponseFactory.formulario("Sessão aberta por " + sessao.getDuracaoMinutos() + " minuto(s)",
                        sessao.getSessaoId())
        );
    }

    @GetMapping("/{id}/resultado")
    public ResponseEntity<?> resultado(@PathVariable Long id) {
        ResultadoVotacao resultado = pautaService.obterResultado(id);

        return ResponseEntity.ok(
                apiResponseFactory.selecao("Resultado da votação", resultado)
        );
    }
}
