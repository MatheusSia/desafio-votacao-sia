package com.coop.cooperative.service;

import com.coop.cooperative.entity.ResultadoVotacaoAggregate;
import com.coop.cooperative.entity.Voto;
import com.coop.cooperative.entity.SessaoVotacao;
import com.coop.cooperative.exception.BusinessException;
import com.coop.cooperative.exception.ResourceNotFoundException;
import com.coop.cooperative.repository.PautaRepository;
import com.coop.cooperative.repository.ResultadoRepository;
import com.coop.cooperative.repository.SessaoRepository;
import com.coop.cooperative.repository.VotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VotoService {

    private final VotoRepository votoRepository;
    private final SessaoRepository sessaoRepository;
    private final PautaRepository pautaRepository;
    private final ResultadoRepository resultadoRepository;

    public VotoService(VotoRepository votoRepository,
                       SessaoRepository sessaoRepository,
                       PautaRepository pautaRepository,
                       ResultadoRepository resultadoRepository) {
        this.votoRepository = votoRepository;
        this.sessaoRepository = sessaoRepository;
        this.pautaRepository = pautaRepository;
        this.resultadoRepository = resultadoRepository;
    }

    @Transactional
    public Voto registrarVoto(Long associadoId, Long pautaId, String opcaoStr) {
        pautaRepository.findById(pautaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada: " + pautaId));

        SessaoVotacao sessao = sessaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new BusinessException("Sessão de votação inexistente para pauta: " + pautaId));

        if (!sessao.isAberta()) {
            throw new BusinessException("Sessão de votação fechada para pauta: " + pautaId);
        }

        String associadoIdStr = String.valueOf(associadoId);

        Voto.OpcaoVoto opcao;
        try {
            opcao = Voto.OpcaoVoto.valueOf(opcaoStr.trim().toUpperCase());
        } catch (Exception e) {
            throw new BusinessException("Opção inválida. Use 'SIM' ou 'NAO'.");
        }

        Voto voto = new Voto(pautaId, associadoIdStr, opcao);

        try {
            voto = votoRepository.saveAndFlush(voto);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new BusinessException("Associado já votou nesta pauta: " + associadoIdStr);
        } catch (Exception ex) {
            throw new BusinessException("Erro inesperado ao registrar voto: " + ex.getMessage());
        }

        int updated = 0;
        try {
            if (opcao == Voto.OpcaoVoto.SIM) {
                updated = resultadoRepository.incrementSim(pautaId);
            } else {
                updated = resultadoRepository.incrementNao(pautaId);
            }
        } catch (Exception ex) {
            throw new BusinessException("Erro ao atualizar agregado de votos: " + ex.getMessage());
        }

        if (updated == 0) {
            try {
                ResultadoVotacaoAggregate r = new ResultadoVotacaoAggregate(
                        pautaId,
                        opcao == Voto.OpcaoVoto.SIM ? 1L : 0L,
                        opcao == Voto.OpcaoVoto.NAO ? 1L : 0L
                );
                resultadoRepository.save(r);
            } catch (org.springframework.dao.DataIntegrityViolationException ex) {
                try {
                    if (opcao == Voto.OpcaoVoto.SIM) resultadoRepository.incrementSim(pautaId);
                    else resultadoRepository.incrementNao(pautaId);
                } catch (Exception ex2) {
                    throw new BusinessException("Erro ao atualizar agregado após concorrência: " + ex2.getMessage());
                }
            } catch (Exception ex) {
                throw new BusinessException("Erro inesperado ao criar agregado de votos: " + ex.getMessage());
            }
        }

        return voto;
    }
}
