package com.coop.cooperative.repository;

import com.coop.cooperative.entity.SessaoVotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SessaoRepository extends JpaRepository<SessaoVotacao, Long> {
    Optional<SessaoVotacao> findByPautaId(Long pautaId);
}
