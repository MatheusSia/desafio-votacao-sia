package com.coop.cooperative.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pauta")
public class Pauta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    @Column(length = 2000)
    private String descricao;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne(mappedBy = "pauta", fetch = FetchType.LAZY)
    private SessaoVotacao sessao;

    @OneToOne(mappedBy = "pauta", fetch = FetchType.LAZY)
    private ResultadoVotacaoAggregate resultado;

    @OneToMany(mappedBy = "pauta", fetch = FetchType.LAZY)
    private List<Voto> votos = new ArrayList<>();

    public Pauta() {}

    public Pauta(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public SessaoVotacao getSessao() { return sessao; }
    public void setSessao(SessaoVotacao sessao) { this.sessao = sessao; }
    public ResultadoVotacaoAggregate getResultado() { return resultado; }
    public void setResultado(ResultadoVotacaoAggregate resultado) { this.resultado = resultado; }
    public List<Voto> getVotos() { return votos; }
    public void setVotos(List<Voto> votos) { this.votos = votos; }
}
