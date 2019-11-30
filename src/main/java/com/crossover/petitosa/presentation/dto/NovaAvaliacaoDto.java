package com.crossover.petitosa.presentation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NovaAvaliacaoDto {

    @NotNull
    @Min(1)
    @Max(5)
    @ApiModelProperty(example = "4", notes = "Nota de 1 a 5")
    private Double nota;

    @Size(max = 240)
    @ApiModelProperty(example = "Gostei muito do serviço, bem atencioso com meu animalzinho")
    private String texto;

}
