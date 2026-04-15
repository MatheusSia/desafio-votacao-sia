package com.coop.cooperative.service;

import com.coop.cooperative.dto.SessaoAberturaResponse;
import com.coop.cooperative.entity.Pauta;
import com.coop.cooperative.entity.SessaoVotacao;
import com.coop.cooperative.exception.BusinessException;
import com.coop.cooperative.exception.ResourceNotFoundException;
import com.coop.cooperative.repository.PautaRepository;
import com.coop.cooperative.repository.ResultadoRepository;
import com.coop.cooperative.repository.SessaoRepository;
import com.coop.cooperative.repository.VotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PautaServiceUnitTest {

    @Mock
    private PautaRepository pautaRepository;
    @Mock
    private SessaoRepository sessaoRepository;
    @Mock
    private VotoRepository votoRepository;
    @Mock
    private ResultadoRepository resultadoRepository;

    @InjectMocks
    private PautaService pautaService;

    @Test
    void deveAbrirSessaoComDuracaoPadraoQuandoMinutosForNulo() {
        Pauta pauta = new Pauta("Teste", "Descricao");
        pauta.setId(10L);

        when(pautaRepository.findById(10L)).thenReturn(Optional.of(pauta));
        when(sessaoRepository.findByPautaId(10L)).thenReturn(Optional.empty());
        when(sessaoRepository.save(any(SessaoVotacao.class))).thenAnswer(invocation -> {
            SessaoVotacao sessao = invocation.getArgument(0);
            sessao.setId(99L);
            return sessao;
        });

        SessaoAberturaResponse response = pautaService.abrirSessaoComResumo(10L, null);

        assertThat(response.getSessaoId()).isEqualTo(99L);
        assertThat(response.getDuracaoMinutos()).isEqualTo(1L);
    }

    @Test
    void deveLancarErroQuandoPautaNaoExistir() {
        when(pautaRepository.findById(123L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pautaService.abrirSessaoComResumo(123L, 5))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Pauta não encontrada");
    }

    @Test
    void deveLancarErroQuandoSessaoJaExistirParaPauta() {
        Pauta pauta = new Pauta("Teste", "Descricao");
        pauta.setId(10L);
        SessaoVotacao existente = new SessaoVotacao(10L, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1));

        when(pautaRepository.findById(10L)).thenReturn(Optional.of(pauta));
        when(sessaoRepository.findByPautaId(10L)).thenReturn(Optional.of(existente));

        assertThatThrownBy(() -> pautaService.abrirSessaoComResumo(10L, 2))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Já existe uma sessão");
    }
}
