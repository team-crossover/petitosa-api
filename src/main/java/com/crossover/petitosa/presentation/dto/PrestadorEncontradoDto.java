package com.crossover.petitosa.presentation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;

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
    @Min(0)
    private BigDecimal precoServico;

    @NotNull
    @Min(0)
    private BigDecimal precoTaxaPetitosa;

    @NotNull
    @Min(0)
    private BigDecimal precoTaxaDesistencia;

    @NotNull
    @Min(0)
    private BigDecimal precoTotal;

}
