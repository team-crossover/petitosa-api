package com.crossover.petitosa.presentation.dto;

import com.crossover.petitosa.business.entity.Servico;
import com.crossover.petitosa.business.enums.StatusServico;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.ElementCollection;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
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

    private String nomeContratante;

    @NotNull
    private Long idPrestador;

    private String nomePrestador;

    @NotNull
    @ApiModelProperty(notes = "Endereço onde será/foi realizado o serviço, pode ser diferente do endereço atual do contratante")
    private EnderecoDto enderecoEsperado;

    @Size(max = 500)
    private String observacoes;

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

    @NotNull
    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    @ApiModelProperty(example = "23:59 31/12/1998")
    private LocalDateTime dataSolicitacao;

    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    @ApiModelProperty(example = "23:59 31/12/1998")
    private LocalDateTime dataAceitacao;

    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    @ApiModelProperty(example = "23:59 31/12/1998")
    private LocalDateTime dataRejeicao;

    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    @ApiModelProperty(example = "23:59 31/12/1998")
    private LocalDateTime dataDesistencia;

    @NotNull
    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    @ApiModelProperty(example = "23:59 31/12/1998")
    private LocalDateTime dataEsperadaRealizacao;

    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    @ApiModelProperty(example = "23:59 31/12/1998")
    private LocalDateTime dataInicioRealizacao;

    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    @ApiModelProperty(example = "23:59 31/12/1998")
    private LocalDateTime dataTerminoRealizacao;

    @NotNull
    private StatusServico status;

    @Builder.Default
    @NotNull
    @Size(min = 1)
    @ElementCollection
    @ApiModelProperty(notes = "Lista de animais, cada um associado aos tipos de serviços que receberá")
    private ServicosPorAnimalDto[] servicosPorAnimais = new ServicosPorAnimalDto[0];

    private AvaliacaoDto avaliacao;

    public static ServicoDto fromServico(Servico servico) {
        return ServicoDto.builder()
                .id(servico.getId())
                .idContratante(servico.getContratante().getId())
                .nomeContratante(servico.getContratante().getNome())
                .idPrestador(servico.getPrestador().getId())
                .nomePrestador(servico.getPrestador().getNome())
                .enderecoEsperado(EnderecoDto.fromEndereco(servico.getEnderecoServico()))
                .observacoes(servico.getObservacoes())
                .precoServico(servico.getPrecoServico())
                .precoTaxaPetitosa(servico.getPrecoTaxaPetitosa())
                .precoTaxaDesistencia(servico.getPrecoTaxaDesistencia())
                .precoTotal(servico.getPrecoTotal())
                .dataSolicitacao(servico.getDataSolicitacao())
                .dataAceitacao(servico.getDataAceitacao())
                .dataRejeicao(servico.getDataRejeicao())
                .dataDesistencia(servico.getDataDesistencia())
                .dataEsperadaRealizacao(servico.getDataEsperadaRealizacao())
                .dataInicioRealizacao(servico.getDataInicioRealizacao())
                .dataTerminoRealizacao(servico.getDataTerminoRealizacao())
                .status(servico.getStatus())
                .servicosPorAnimais(servico.getServicosPorAnimais().stream().map(ServicosPorAnimalDto::fromServicosPorAnimal).toArray(ServicosPorAnimalDto[]::new))
                .avaliacao(servico.getAvaliacao() == null ? null : AvaliacaoDto.fromAvaliacao(servico.getAvaliacao()))
                .build();
    }

}
