package com.coop.cooperative.repository;

import com.coop.cooperative.entity.ResultadoVotacaoAggregate;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ResultadoRepository extends JpaRepository<ResultadoVotacaoAggregate, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE ResultadoVotacaoAggregate r SET r.totalSim = r.totalSim + 1 WHERE r.pautaId = :pautaId")
    int incrementSim(@Param("pautaId") Long pautaId);

    @Modifying
    @Transactional
    @Query("UPDATE ResultadoVotacaoAggregate r SET r.totalNao = r.totalNao + 1 WHERE r.pautaId = :pautaId")
    int incrementNao(@Param("pautaId") Long pautaId);
}
