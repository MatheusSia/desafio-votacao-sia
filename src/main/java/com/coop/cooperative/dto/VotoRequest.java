package com.coop.cooperative.dto;

public class VotoRequest {
    private Long associadoId;
    private Long pautaId;
    private String opcao; // "SIM" ou "NAO"

    public Long getAssociadoId() { return associadoId; }
    public void setAssociadoId(Long associadoId) { this.associadoId = associadoId; }
    public Long getPautaId() { return pautaId; }
    public void setPautaId(Long pautaId) { this.pautaId = pautaId; }
    public String getOpcao() { return opcao; }
    public void setOpcao(String opcao) { this.opcao = opcao; }
}
