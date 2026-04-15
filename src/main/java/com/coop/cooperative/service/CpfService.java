package com.coop.cooperative.service;

import com.coop.cooperative.dto.StatusResponse;
import com.coop.cooperative.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CpfService {

    private final CpfValidationGateway cpfValidationGateway;

    public CpfService(CpfValidationGateway cpfValidationGateway) {
        this.cpfValidationGateway = cpfValidationGateway;
    }

    public StatusResponse validarCpf(String cpf) {
        try {
            return cpfValidationGateway.validateCpfStatus(cpf);
        } catch (IllegalArgumentException ex) {
            throw new ResourceNotFoundException(ex.getMessage());
        }
    }
}
