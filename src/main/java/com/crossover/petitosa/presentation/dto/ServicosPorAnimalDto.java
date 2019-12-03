package com.crossover.petitosa.presentation.dto;

import com.crossover.petitosa.business.entity.ServicosPorAnimal;
import com.crossover.petitosa.business.enums.EspecieAnimal;
import com.crossover.petitosa.business.enums.PorteAnimal;
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

    private String apelidoAnimal;

    private EspecieAnimal especieAnimal;

    private PorteAnimal porteAnimal;

    @ApiModelProperty(notes = "Tipos de serviços que o animal recebe", example = "[ TOSA, BANHO ]")
    @Builder.Default
    @NotNull
    @Size(min = 1)
    @ElementCollection
    private TipoServico[] tiposServicos = new TipoServico[0];

    public static ServicosPorAnimalDto fromServicosPorAnimal(ServicosPorAnimal servicosPorAnimal) {
        return ServicosPorAnimalDto.builder()
                .idAnimal(servicosPorAnimal.getAnimal().getId())
                .apelidoAnimal(servicosPorAnimal.getAnimal().getApelido())
                .especieAnimal(servicosPorAnimal.getAnimal().getEspecie())
                .porteAnimal(servicosPorAnimal.getAnimal().getPorte())
                .tiposServicos(servicosPorAnimal.getTiposServico().toArray(new TipoServico[0]))
                .build();
    }
}
