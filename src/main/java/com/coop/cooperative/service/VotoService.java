package com.coop.cooperative.service;

import com.coop.cooperative.entity.Voto;
import com.coop.cooperative.entity.SessaoVotacao;
import com.coop.cooperative.exception.BusinessException;
import com.coop.cooperative.exception.ResourceNotFoundException;
import com.coop.cooperative.repository.PautaRepository;
import com.coop.cooperative.repository.SessaoRepository;
import com.coop.cooperative.repository.VotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VotoService {

    private final VotoRepository votoRepository;
    private final SessaoRepository sessaoRepository;
    private final PautaRepository pautaRepository;

    public VotoService(VotoRepository votoRepository,
                       SessaoRepository sessaoRepository,
                       PautaRepository pautaRepository) {
        this.votoRepository = votoRepository;
        this.sessaoRepository = sessaoRepository;
        this.pautaRepository = pautaRepository;
    }

    /**
     * Registrar voto.
     * Note que no banco a coluna associadoId é string (flexível) — convertemos aqui.
     *
     * @param associadoId identificador do associado (Long no contract)
     * @param pautaId id da pauta
     * @param opcaoStr "SIM" / "NAO" (case-insensitive)
     */
    @Transactional
    public Voto registrarVoto(Long associadoId, Long pautaId, String opcaoStr) {
        // valida pauta
        pautaRepository.findById(pautaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada: " + pautaId));

        // valida sessão
        SessaoVotacao sessao = sessaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new BusinessException("Sessão de votação inexistente para pauta: " + pautaId));

        if (!sessao.isAberta()) {
            throw new BusinessException("Sessão de votação fechada para pauta: " + pautaId);
        }

        String associadoIdStr = String.valueOf(associadoId);

        votoRepository.findByPautaIdAndAssociadoId(pautaId, associadoIdStr).ifPresent(v -> {
            throw new BusinessException("Associado já votou nesta pauta: " + associadoIdStr);
        });

        Voto.OpcaoVoto opcao;
        try {
            opcao = Voto.OpcaoVoto.valueOf(opcaoStr.trim().toUpperCase());
        } catch (Exception e) {
            throw new BusinessException("Opção inválida. Use 'SIM' ou 'NAO'.");
        }

        Voto voto = new Voto(pautaId, associadoIdStr, opcao);
        return votoRepository.save(voto);
    }
}
