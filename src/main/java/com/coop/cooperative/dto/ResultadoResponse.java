package com.coop.cooperative.dto;

public class ResultadoResponse {
    private String tipoTela; // FORMULARIO ou SELECAO
    private String mensagem;
    private Object dados;

    public ResultadoResponse(String tipoTela, String mensagem, Object dados) {
        this.tipoTela = tipoTela;
        this.mensagem = mensagem;
        this.dados = dados;
    }

    public String getTipoTela() { return tipoTela; }
    public String getMensagem() { return mensagem; }
    public Object getDados() { return dados; }
}
