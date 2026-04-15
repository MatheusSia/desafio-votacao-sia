package com.coop.cooperative.service;

import com.coop.cooperative.dto.ResultadoResponse;
import org.springframework.stereotype.Component;

@Component
public class ApiResponseFactory {

    public ResultadoResponse formulario(String mensagem, Object dados) {
        return new ResultadoResponse("FORMULARIO", mensagem, dados);
    }

    public ResultadoResponse selecao(String mensagem, Object dados) {
        return new ResultadoResponse("SELECAO", mensagem, dados);
    }
}
