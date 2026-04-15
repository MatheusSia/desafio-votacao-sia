package com.coop.cooperative;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveRetornarBadRequestQuandoCriarPautaSemTitulo() throws Exception {
        mockMvc.perform(post("/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descricao\":\"sem titulo\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveExecutarFluxoCompletoDeVotacao() throws Exception {
        MvcResult createPautaResult = mockMvc.perform(post("/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"Pauta Integrada\",\"descricao\":\"Fluxo completo\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dados").isNumber())
                .andReturn();

        Long pautaId = extractDadosAsLong(createPautaResult);

        mockMvc.perform(post("/pautas/{id}/sessoes", pautaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"minutos\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dados").isNumber());

        mockMvc.perform(post("/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"associadoId\":987654,\"pautaId\":" + pautaId + ",\"opcao\":\"SIM\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/pautas/{id}/resultado", pautaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dados.pautaId").value(pautaId))
                .andExpect(jsonPath("$.dados.totalSim").value(1))
                .andExpect(jsonPath("$.dados.totalNao").value(0))
                .andExpect(jsonPath("$.dados.status").value("ABERTA"));
    }

    private Long extractDadosAsLong(MvcResult result) throws Exception {
        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        JsonNode dados = body.get("dados");
        assertThat(dados).isNotNull();
        return dados.asLong();
    }
}
