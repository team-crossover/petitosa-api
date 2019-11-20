package com.crossover.petitosa.presentation.dto;

import com.crossover.petitosa.business.enums.EspecieAnimal;
import com.crossover.petitosa.business.enums.PorteAnimal;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NovoAnimalDto {

    @NotNull
    @ApiModelProperty(example = "GATO", allowableValues = "CACHORRO, GATO")
    private EspecieAnimal especie;

    @NotNull
    @ApiModelProperty(example = "MEDIO", allowableValues = "PEQUENO, MEDIO, GRANDE")
    private PorteAnimal porte;

    @ApiModelProperty(example = "Fila Brasileiro")
    private String raca;

    @NotBlank
    @ApiModelProperty(example = "Bilu")
    private String apelido;

    @Size(max = 500)
    @ApiModelProperty(example = "Ele tem problema de respiração")
    private String observacoes;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @ApiModelProperty(example = "31/12/2010")
    private LocalDate dataNascimento;

    @ApiModelProperty(notes = "Opcional, se tiver em branco será ignorado")
    @Basic(fetch = FetchType.LAZY)
    @Size(max = 10485760) // 10 MB
    private String imgAnimal;

}
