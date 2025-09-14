package com.coop.cooperative.service;

import com.coop.cooperative.entity.SessaoVotacao;
import com.coop.cooperative.exception.BusinessException;
import com.coop.cooperative.repository.SessaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class SessaoService {
    private final SessaoRepository sessaoRepository;

    public SessaoService(SessaoRepository sessaoRepository) {
        this.sessaoRepository = sessaoRepository;
    }

    public SessaoVotacao abrirSessao(Long pautaId, Long duracaoSegundos) {
        // se já existir sessão para a pauta e ainda estiver aberta, lançar erro
        sessaoRepository.findByPautaId(pautaId).ifPresent(existing -> {
            if (existing.getFim().isAfter(LocalDateTime.now())) {
                throw new BusinessException("Sessão já aberta para a pauta: " + pautaId);
            }
        });

        LocalDateTime inicio = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        long dur = duracaoSegundos != null && duracaoSegundos > 0 ? duracaoSegundos : 60L;
        LocalDateTime fim = inicio.plusSeconds(dur);

        SessaoVotacao sessao = new SessaoVotacao(pautaId, inicio, fim);
        return sessaoRepository.save(sessao);
    }

    public SessaoVotacao obterSessaoPorPauta(Long pautaId) {
        return sessaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new BusinessException("Sessão não encontrada para pauta: " + pautaId));
    }

    public boolean estaAberta(Long pautaId) {
        return sessaoRepository.findByPautaId(pautaId)
                .map(SessaoVotacao::isAberta)
                .orElse(false);
    }
}
