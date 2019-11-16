package com.crossover.petitosa.presentation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NovoEnderecoDto {

    @NotBlank
    @ApiModelProperty(example = "Rua C 95")
    private String logradouro;

    @NotNull
    @Min(0)
    @Max(99999999)
    @ApiModelProperty(example = "74303360")
    private Integer cep;

}
