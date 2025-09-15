package com.coop.cooperative.service;

import com.coop.cooperative.exception.BusinessException;
import com.coop.cooperative.entity.Pauta;
import com.coop.cooperative.repository.PautaRepository;
import com.coop.cooperative.repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class VotoServiceTest {

    @Autowired
    private VotoService votoService;

    @Autowired
    private SessaoService sessaoService;

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    private Pauta pauta;

    @BeforeEach
    void setup() {
        pauta = new Pauta("Pauta Teste", "Testando votos");
        pautaRepository.save(pauta);

        // Abre a sessão de votação para a pauta, duração padrão 60 segundos
        sessaoService.abrirSessao(pauta.getId(), null);
    }

    @Test
    void naoDevePermitirDoisVotosDoMesmoAssociadoNaMesmaPauta() {
        Long associadoId = 1L;

        // Primeiro voto deve passar
        votoService.registrarVoto(associadoId, pauta.getId(), "SIM");

        // Segundo voto deve falhar
        assertThatThrownBy(() -> votoService.registrarVoto(associadoId, pauta.getId(), "NAO"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("já votou");
    }
}
