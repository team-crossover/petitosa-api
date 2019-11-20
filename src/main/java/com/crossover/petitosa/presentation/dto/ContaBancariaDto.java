package com.crossover.petitosa.presentation.dto;

import com.crossover.petitosa.business.entity.ContaBancaria;
import com.crossover.petitosa.business.enums.TipoConta;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {})
public class ContaBancariaDto {

    private Long id;

    @ApiModelProperty(example = "CORRENTE")
    @NotNull
    private TipoConta tipo;

    @ApiModelProperty(example = "48935")
    @NotBlank
    @Pattern(regexp = "^\\d*$")
    @Size(min = 1)
    private String numero;

    @ApiModelProperty(example = "7")
    @NotBlank
    @Pattern(regexp = "^\\d$")
    private String digito;

    @ApiModelProperty(example = "1598")
    @NotBlank
    @Pattern(regexp = "^\\d*$")
    @Size(min = 1)
    private String agencia;

    public static ContaBancariaDto fromConta(ContaBancaria contaBancaria) {
        return ContaBancariaDto.builder()
                .id(contaBancaria.getId())
                .tipo(contaBancaria.getTipo())
                .numero(contaBancaria.getNumero())
                .digito(contaBancaria.getDigito())
                .agencia(contaBancaria.getAgencia())
                .build();
    }
}
