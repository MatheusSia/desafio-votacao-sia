package com.coop.cooperative.dto;

public class CriarPautaRequest {
    private String titulo;
    private String descricao; // opcional

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
