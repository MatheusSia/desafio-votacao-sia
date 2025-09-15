package com.coop.cooperative.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "resultado_votacao")
public class ResultadoVotacaoAggregate {

    @Id
    @Column(name = "pauta_id")
    private Long pautaId;

    @Column(name = "total_sim", nullable = false)
    private long totalSim;

    @Column(name = "total_nao", nullable = false)
    private long totalNao;

    public ResultadoVotacaoAggregate() {}

    public ResultadoVotacaoAggregate(Long pautaId, long totalSim, long totalNao) {
        this.pautaId = pautaId;
        this.totalSim = totalSim;
        this.totalNao = totalNao;
    }

    public Long getPautaId() { return pautaId; }
    public void setPautaId(Long pautaId) { this.pautaId = pautaId; }

    public long getTotalSim() { return totalSim; }
    public void setTotalSim(long totalSim) { this.totalSim = totalSim; }

    public long getTotalNao() { return totalNao; }
    public void setTotalNao(long totalNao) { this.totalNao = totalNao; }
}
