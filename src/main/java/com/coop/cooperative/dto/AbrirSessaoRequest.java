package com.coop.cooperative.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class AbrirSessaoRequest {
    @Min(value = 1, message = "minutos deve ser maior ou igual a 1")
    @Max(value = 10080, message = "minutos deve ser menor ou igual a 10080")
    private Integer minutos;

    public Integer getMinutos() { return minutos; }
    public void setMinutos(Integer minutos) { this.minutos = minutos; }
}
