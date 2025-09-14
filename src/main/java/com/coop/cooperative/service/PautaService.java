package com.coop.cooperative.service;

import com.coop.cooperative.dto.ResultadoVotacao;
import com.coop.cooperative.entity.Pauta;
import com.coop.cooperative.entity.SessaoVotacao;
import com.coop.cooperative.entity.Voto;
import com.coop.cooperative.exception.BusinessException;
import com.coop.cooperative.exception.ResourceNotFoundException;
import com.coop.cooperative.repository.PautaRepository;
import com.coop.cooperative.repository.SessaoRepository;
import com.coop.cooperative.repository.VotoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class PautaService {

    private final PautaRepository pautaRepository;
    private final SessaoRepository sessaoRepository;
    private final VotoRepository votoRepository;

    public PautaService(PautaRepository pautaRepository,
                        SessaoRepository sessaoRepository,
                        VotoRepository votoRepository) {
        this.pautaRepository = pautaRepository;
        this.sessaoRepository = sessaoRepository;
        this.votoRepository = votoRepository;
    }

    public Pauta criarPauta(String titulo, String descricao) {
        Pauta p = new Pauta();
        p.setTitulo(titulo);
        p.setDescricao(descricao);
        return pautaRepository.save(p);
    }

    /**
     * Abre uma sessão para a pauta. minutos = null -> default 1 minuto.
     */
    public SessaoVotacao abrirSessao(Long pautaId, Integer minutos) {
        Pauta pauta = pautaRepository.findById(pautaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada: " + pautaId));

        // se já existir sessão para a pauta (aberta ou encerrada), lança erro
        Optional<SessaoVotacao> existing = sessaoRepository.findByPautaId(pautaId);
        if (existing.isPresent()) {
            throw new BusinessException("Já existe uma sessão cadastrada para a pauta: " + pautaId);
        }

        LocalDateTime inicio = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        int mins = (minutos == null || minutos <= 0) ? 1 : minutos;
        LocalDateTime fim = inicio.plusMinutes(mins);

        SessaoVotacao sessao = new SessaoVotacao(pautaId, inicio, fim);
        return sessaoRepository.save(sessao);
    }

    /**
     * Retorna contagem de votos e status da sessão (ABERTA / ENCERRADA / SEM_SESSAO)
     */
    public ResultadoVotacao obterResultado(Long pautaId) {
        // valida pauta
        pautaRepository.findById(pautaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada: " + pautaId));

        long sim = votoRepository.countByPautaIdAndOpcao(pautaId, Voto.OpcaoVoto.SIM);
        long nao = votoRepository.countByPautaIdAndOpcao(pautaId, Voto.OpcaoVoto.NAO);

        String status = sessaoRepository.findByPautaId(pautaId)
                .map(s -> s.isAberta() ? "ABERTA" : "ENCERRADA")
                .orElse("SEM_SESSAO");

        String resultado;
        if (sim > nao) resultado = "APROVADA";
        else if (nao > sim) resultado = "REJEITADA";
        else resultado = "EMPATE";

        return new ResultadoVotacao(pautaId, sim, nao, status, resultado);
    }
}
