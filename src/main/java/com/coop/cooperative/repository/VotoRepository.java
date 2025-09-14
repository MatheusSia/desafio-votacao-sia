package com.coop.cooperative.repository;

import com.coop.cooperative.entity.Voto;
import com.coop.cooperative.entity.Voto.OpcaoVoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VotoRepository extends JpaRepository<Voto, Long> {
    Optional<Voto> findByPautaIdAndAssociadoId(Long pautaId, String associadoId);
    List<Voto> findByPautaId(Long pautaId);
    long countByPautaIdAndOpcao(Long pautaId, OpcaoVoto opcao);
}
