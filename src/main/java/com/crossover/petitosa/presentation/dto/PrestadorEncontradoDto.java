package com.crossover.petitosa.presentation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrestadorEncontradoDto {

    @NotNull
    private Long idPrestador;

    @NotBlank
    @ApiModelProperty(example = "Jonas")
    private String nome;

    @Size(max = 1000)
    @ApiModelProperty(example = "Adoro servicosPorAnimais")
    private String descricao;

    private String imgPerfil;

    @NotNull
    @ApiModelProperty(example = "4.9")
    private Double avaliacao;

    @NotNull
    @ApiModelProperty(example = "1500")
    private Double distancia;

    @NotNull
    @ApiModelProperty(example = "59.90")
    private Double precoTotal;

}
