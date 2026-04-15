package com.coop.cooperative.service;

import com.coop.cooperative.dto.StatusResponse;

public interface CpfValidationGateway {
    StatusResponse validateCpfStatus(String cpf);
}
