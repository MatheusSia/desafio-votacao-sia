package com.coop.cooperative.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PautaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveCadastrarPautaComSucesso() throws Exception {
        mockMvc.perform(post("/pautas/cadastrar")
                        .contentType("application/json")
                        .content("{\"titulo\": \"Pauta via Teste\", \"descricao\": \"Teste API\"}"))
                .andExpect(status().isOk());
    }
}
