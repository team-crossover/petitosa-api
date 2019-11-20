package com.crossover.petitosa.presentation.dto;

import com.crossover.petitosa.business.entity.ServicosPorAnimal;
import com.crossover.petitosa.business.enums.TipoServico;
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
public class ServicosPorAnimalDto {

    @ApiModelProperty(notes = "ID do animal que recebe o serviço")
    @NotNull
    private Long idAnimal;

    @ApiModelProperty(notes = "Tipos de serviços que o animal recebe", example = "[ TOSA, BANHO ]")
    @Builder.Default
    @NotNull
    @Size(min = 1)
    @ElementCollection
    private TipoServico[] tiposServicos = new TipoServico[0];

    public static ServicosPorAnimalDto fromServicoPorAnimal(ServicosPorAnimal servicosPorAnimal) {
        return ServicosPorAnimalDto.builder()
                .idAnimal(servicosPorAnimal.getAnimal().getId())
                .tiposServicos(servicosPorAnimal.getTiposServico().toArray(new TipoServico[0]))
                .build();
    }
}
