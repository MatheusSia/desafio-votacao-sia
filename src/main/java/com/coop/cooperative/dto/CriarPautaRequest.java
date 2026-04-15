package com.coop.cooperative.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CriarPautaRequest {
    @NotBlank(message = "titulo é obrigatório")
    @Size(max = 255, message = "titulo deve ter no máximo 255 caracteres")
    private String titulo;

    @Size(max = 2000, message = "descricao deve ter no máximo 2000 caracteres")
    private String descricao;

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
