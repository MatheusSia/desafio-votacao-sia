package com.coop.cooperative.service;

import com.coop.cooperative.client.CpfValidationClient;
import com.coop.cooperative.dto.StatusResponse;
import com.coop.cooperative.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CpfService {

    private final CpfValidationClient cpfValidationClient;

    public CpfService(CpfValidationClient cpfValidationClient) {
        this.cpfValidationClient = cpfValidationClient;
    }

    public StatusResponse validarCpf(String cpf) {
        try {
            return cpfValidationClient.validateCpfStatus(cpf);
        } catch (IllegalArgumentException ex) {
            throw new ResourceNotFoundException(ex.getMessage());
        }
    }
}
