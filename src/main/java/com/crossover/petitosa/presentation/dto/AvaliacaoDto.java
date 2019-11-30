package com.crossover.petitosa.presentation.dto;

import com.crossover.petitosa.business.entity.Avaliacao;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoDto {

    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    @ApiModelProperty(example = "23:59 31/12/1998")
    private LocalDateTime dataAvaliacao;

    @NotNull
    @Min(1)
    @Max(5)
    @ApiModelProperty(example = "4", notes = "Nota de 1 a 5")
    private Double nota;

    @Size(max = 240)
    @ApiModelProperty(example = "Gostei muito do serviço, bem atencioso com meu animalzinho")
    private String texto;

    public static AvaliacaoDto fromAvaliacao(Avaliacao avaliacao) {
        return AvaliacaoDto.builder()
                .dataAvaliacao(avaliacao.getDataAvaliacao())
                .nota(avaliacao.getNota())
                .texto(avaliacao.getTexto())
                .build();
    }
}
