package com.coop.cooperative.repository;

import com.coop.cooperative.entity.Pauta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PautaRepositoryTest {

    @Autowired
    private PautaRepository pautaRepository;

    @Test
    void deveSalvarENcontrarPauta() {
        Pauta pauta = new Pauta();
        pauta.setTitulo("Teste Repository");
        pauta.setDescricao("Descricao de teste");

        Pauta salva = pautaRepository.save(pauta);

        assertThat(salva.getId()).isNotNull();
        assertThat(pautaRepository.findById(salva.getId())).isPresent();
    }
}
