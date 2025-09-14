package com.coop.cooperative.dto;

public class ResultadoVotacao {
    private Long pautaId;
    private long totalSim;
    private long totalNao;
    private String status;   // ABERTA / ENCERRADA / SEM_SESSAO
    private String resultado; // APROVADA / REJEITADA / EMPATE

    public ResultadoVotacao(Long pautaId, long totalSim, long totalNao, String status, String resultado) {
        this.pautaId = pautaId;
        this.totalSim = totalSim;
        this.totalNao = totalNao;
        this.status = status;
        this.resultado = resultado;
    }

    public Long getPautaId() { return pautaId; }
    public long getTotalSim() { return totalSim; }
    public long getTotalNao() { return totalNao; }
    public String getStatus() { return status; }
    public String getResultado() { return resultado; }
}
