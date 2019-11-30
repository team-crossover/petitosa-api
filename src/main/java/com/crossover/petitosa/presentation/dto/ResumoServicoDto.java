package com.crossover.petitosa.presentation.dto;

import com.crossover.petitosa.business.entity.Servico;
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
public class ResumoServicoDto {

    @NotNull
    private Long id;

    @NotNull
    private Long idContratante;

    @NotNull
    private String nomeContratante;

    @NotNull
    private Long idPrestador;

    @NotNull
    private String nomePrestador;

    @NotNull
    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    @ApiModelProperty(example = "23:59 31/12/1998")
    private LocalDateTime dataSolicitacao;

    @Builder.Default
    @NotNull
    @Size(min = 1)
    @ElementCollection
    @ApiModelProperty(notes = "Lista de animais, cada um associado aos tipos de serviços que receberá")
    private ServicosPorAnimalDto[] servicosPorAnimais = new ServicosPorAnimalDto[0];

    private AvaliacaoDto avaliacao;

    public static ResumoServicoDto fromServico(Servico servico) {
        return ResumoServicoDto.builder()
                .id(servico.getId())
                .idContratante(servico.getContratante().getId())
                .nomeContratante(servico.getContratante().getNome())
                .idPrestador(servico.getPrestador().getId())
                .nomePrestador(servico.getPrestador().getNome())
                .dataSolicitacao(servico.getDataSolicitacao())
                .servicosPorAnimais(servico.getServicosPorAnimais().stream().map(ServicosPorAnimalDto::fromServicosPorAnimal).toArray(ServicosPorAnimalDto[]::new))
                .avaliacao(servico.getAvaliacao() == null ? null : AvaliacaoDto.fromAvaliacao(servico.getAvaliacao()))
                .build();
    }

}
