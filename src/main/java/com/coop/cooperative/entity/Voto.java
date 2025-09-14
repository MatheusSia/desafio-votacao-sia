package com.coop.cooperative.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "voto",
        uniqueConstraints = @UniqueConstraint(columnNames = {"pautaId", "associadoId"}))
public class Voto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pautaId;
    private String associadoId; // id único do associado (string para flexibilidade)
    @Enumerated(EnumType.STRING)
    private OpcaoVoto opcao;
    private LocalDateTime criadoEm = LocalDateTime.now();

    public Voto() {}

    public Voto(Long pautaId, String associadoId, OpcaoVoto opcao) {
        this.pautaId = pautaId;
        this.associadoId = associadoId;
        this.opcao = opcao;
    }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPautaId() { return pautaId; }
    public void setPautaId(Long pautaId) { this.pautaId = pautaId; }
    public String getAssociadoId() { return associadoId; }
    public void setAssociadoId(String associadoId) { this.associadoId = associadoId; }
    public OpcaoVoto getOpcao() { return opcao; }
    public void setOpcao(OpcaoVoto opcao) { this.opcao = opcao; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

    public enum OpcaoVoto {
        SIM, NAO
    }
}
