package com.coop.cooperative.service;

import com.coop.cooperative.dto.StatusResponse;
import com.coop.cooperative.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CpfServiceTest {

    @Mock
    private CpfValidationGateway cpfValidationGateway;

    @InjectMocks
    private CpfService cpfService;

    @Test
    void deveRetornarStatusQuandoCpfForValido() {
        when(cpfValidationGateway.validateCpfStatus("12345678909"))
                .thenReturn(new StatusResponse("ABLE_TO_VOTE"));

        StatusResponse status = cpfService.validarCpf("12345678909");

        assertThat(status).isNotNull();
        assertThat(status.getStatus()).isEqualTo("ABLE_TO_VOTE");
    }

    @Test
    void deveTraduzirErroDeCpfInvalidoParaResourceNotFound() {
        when(cpfValidationGateway.validateCpfStatus("123"))
                .thenThrow(new IllegalArgumentException("CPF inválido"));

        assertThatThrownBy(() -> cpfService.validarCpf("123"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("CPF inválido");
    }
}
