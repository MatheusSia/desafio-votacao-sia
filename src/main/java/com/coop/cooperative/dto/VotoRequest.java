package com.coop.cooperative.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class VotoRequest {
    @NotNull(message = "associadoId é obrigatório")
    @Positive(message = "associadoId deve ser maior que zero")
    private Long associadoId;

    @NotNull(message = "pautaId é obrigatório")
    @Positive(message = "pautaId deve ser maior que zero")
    private Long pautaId;

    @NotBlank(message = "opcao é obrigatória")
    @Pattern(regexp = "(?i)SIM|NAO", message = "opcao deve ser 'SIM' ou 'NAO'")
    private String opcao;

    public Long getAssociadoId() { return associadoId; }
    public void setAssociadoId(Long associadoId) { this.associadoId = associadoId; }
    public Long getPautaId() { return pautaId; }
    public void setPautaId(Long pautaId) { this.pautaId = pautaId; }
    public String getOpcao() { return opcao; }
    public void setOpcao(String opcao) { this.opcao = opcao; }
}
