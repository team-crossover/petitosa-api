package com.crossover.petitosa.presentation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.ElementCollection;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {})
public class FiltroServicoDto {

    @NotNull
    private Long idContratante;

    @Builder.Default
    @NotNull
    @Size(min = 1)
    @ElementCollection
    @ApiModelProperty(notes = "Lista de animais, cada um associado aos tipos de serviços que receberá")
    private ServicosPorAnimalDto[] servicosPorAnimais = new ServicosPorAnimalDto[0];

    @ApiModelProperty(notes = "Em metros", example = "1000")
    private Double distanciaMaxima;

    @ApiModelProperty(notes = "Em reais", example = "79.99")
    private Double precoTotalMaximo;

}
