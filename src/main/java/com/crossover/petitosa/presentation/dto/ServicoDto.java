package com.crossover.petitosa.presentation.dto;

import com.crossover.petitosa.business.enums.StatusServico;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.ElementCollection;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {})
public class ServicoDto {

    @NotNull
    private Long id;

    @NotNull
    private Long idContratante;

    @NotNull
    private Long idPrestador;

    @Builder.Default
    @NotNull
    @Size(min = 1)
    @ElementCollection
    @ApiModelProperty(notes = "Lista de animais, cada um associado aos tipos de serviços que receberá")
    private ServicosPorAnimalDto[] servicosPorAnimais = new ServicosPorAnimalDto[0];

    @NotNull
    @JsonFormat(pattern = "hh:mm dd/MM/yyyy")
    @ApiModelProperty(example = "23:59 31/12/1998")
    private LocalDateTime dataAgendado;

    @NotNull
    private NovoEnderecoDto enderecoAgendado;

    @Size(max = 500)
    private String observacoes;

    @NotNull
    @JsonFormat(pattern = "hh:mm dd/MM/yyyy")
    @ApiModelProperty(example = "23:59 31/12/1998")
    private LocalDateTime dataSolicitado;

    @NotNull
    private StatusServico status;

    @NotNull
    private Double valorTotal;

}
