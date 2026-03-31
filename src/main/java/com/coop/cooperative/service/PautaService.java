package com.coop.cooperative.service;

import com.coop.cooperative.dto.ResultadoVotacao;
import com.coop.cooperative.entity.Pauta;
import com.coop.cooperative.entity.ResultadoVotacaoAggregate;
import com.coop.cooperative.entity.SessaoVotacao;
import com.coop.cooperative.entity.Voto;
import com.coop.cooperative.exception.BusinessException;
import com.coop.cooperative.exception.ResourceNotFoundException;
import com.coop.cooperative.repository.PautaRepository;
import com.coop.cooperative.repository.ResultadoRepository;
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
    private final ResultadoRepository resultadoRepository;

    public PautaService(PautaRepository pautaRepository,
                        SessaoRepository sessaoRepository,
                        VotoRepository votoRepository,
                        ResultadoRepository resultadoRepository) {
        this.pautaRepository = pautaRepository;
        this.sessaoRepository = sessaoRepository;
        this.votoRepository = votoRepository;
        this.resultadoRepository = resultadoRepository;
    }

    public Pauta criarPauta(String titulo, String descricao) {
        Pauta p = new Pauta();
        p.setTitulo(titulo);
        p.setDescricao(descricao);
        return pautaRepository.save(p);
    }

    public SessaoVotacao abrirSessao(Long pautaId, Integer minutos) {
        Pauta pauta = pautaRepository.findById(pautaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada: " + pautaId));

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

    public ResultadoVotacao obterResultado(Long pautaId) {
        pautaRepository.findById(pautaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada: " + pautaId));

        ResultadoVotacaoAggregate agg = resultadoRepository.findById(pautaId).orElse(null);
        if (agg == null) {
            long sim = votoRepository.countByPautaIdAndOpcao(pautaId, Voto.OpcaoVoto.SIM);
            long nao = votoRepository.countByPautaIdAndOpcao(pautaId, Voto.OpcaoVoto.NAO);
            agg = new ResultadoVotacaoAggregate(pautaId, sim, nao);
            try {
                resultadoRepository.save(agg);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                agg = resultadoRepository.findById(pautaId)
                        .orElseThrow(() ->
                                new BusinessException("Erro ao salvar resultado de votação: " + e.getMessage())
                        );
            } catch (Exception e) {
                throw new BusinessException("Erro inesperado ao salvar resultado de votação: " + e.getMessage());
            }

        }

        String status = sessaoRepository.findByPautaId(pautaId)
                .map(s -> s.isAberta() ? "ABERTA" : "ENCERRADA")
                .orElse("SEM_SESSAO");

        String resultado;
        if ("ABERTA".equals(status)) {
            resultado = "VOTAÇÃO EM ANDAMENTO";
        } else {
            if (agg.getTotalSim() > agg.getTotalNao()) resultado = "APROVADA";
            else if (agg.getTotalNao() > agg.getTotalSim()) resultado = "REJEITADA";
            else resultado = "PAUTA EMPATADA (Aguardando voto do presidente da cooperativa)";
        }

        return new ResultadoVotacao(pautaId, agg.getTotalSim(), agg.getTotalNao(), status, resultado);
    }
}
