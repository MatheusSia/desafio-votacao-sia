package com.coop.cooperative.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessao_votacao")
public class SessaoVotacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pautaId;

    private LocalDateTime inicio;
    private LocalDateTime fim;

    public SessaoVotacao() {}

    public SessaoVotacao(Long pautaId, LocalDateTime inicio, LocalDateTime fim) {
        this.pautaId = pautaId;
        this.inicio = inicio;
        this.fim = fim;
    }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPautaId() { return pautaId; }
    public void setPautaId(Long pautaId) { this.pautaId = pautaId; }
    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }
    public LocalDateTime getFim() { return fim; }
    public void setFim(LocalDateTime fim) { this.fim = fim; }

    public boolean isAberta() {
        LocalDateTime now = LocalDateTime.now();
        return (inicio != null && fim != null) && now.isAfter(inicio) && now.isBefore(fim);
    }
}
