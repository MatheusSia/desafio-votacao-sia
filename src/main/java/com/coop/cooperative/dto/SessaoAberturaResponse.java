package com.coop.cooperative.dto;

public class SessaoAberturaResponse {
    private final Long sessaoId;
    private final long duracaoMinutos;

    public SessaoAberturaResponse(Long sessaoId, long duracaoMinutos) {
        this.sessaoId = sessaoId;
        this.duracaoMinutos = duracaoMinutos;
    }

    public Long getSessaoId() {
        return sessaoId;
    }

    public long getDuracaoMinutos() {
        return duracaoMinutos;
    }
}
