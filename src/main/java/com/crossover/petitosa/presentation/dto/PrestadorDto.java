package com.crossover.petitosa.presentation.dto;

import com.crossover.petitosa.business.entity.Prestador;
import com.crossover.petitosa.business.service.PrestadorService;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrestadorDto {

    @NotNull
    private Long id;

    private Long idUsuario;

    @NotBlank
    private String email;

    @NotBlank
    @ApiModelProperty(example = "Jonas")
    private String nome;

    @ApiModelProperty(example = "M")
    private String genero;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @ApiModelProperty(example = "31/12/1998")
    private LocalDate dataNascimento;

    @Size(max = 1000)
    @ApiModelProperty(example = "Adoro servicosPorAnimais")
    private String descricao;

    @NotNull
    private EnderecoDto endereco;

    @NotNull
    @Size(min = 15, max = 15)
    @ElementCollection
    @Builder.Default
    @ApiModelProperty(example = "[true, true, true, true, true, true, false, false, false, true, true, true, true, true, true]")
    private Boolean[] servicosPrestados = new Boolean[15];

    @Builder.Default
    @NotNull
    @Size(min = 15, max = 15)
    @ElementCollection
    @ApiModelProperty(example = "[20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20]")
    private BigDecimal[] precos = new BigDecimal[15];

    private String imgPerfil;

    @NotNull
    private ContaBancariaDto contaBancaria;

    private ResumoServicoDto[] servicos;

    @NotNull
    @ApiModelProperty(example = "4", notes = "Nota média do prestador, de 1 a 5")
    private double avaliacao;

    @ApiModelProperty("Comentários recebidos pelo prestador em avaliações")
    private String[] comentarios;

    public static PrestadorDto fromPrestador(Prestador prestador, PrestadorService prestadorService) {
        return PrestadorDto.builder()
                .id(prestador.getId())
                .idUsuario(prestador.getUsuario().getId())
                .email(prestador.getUsuario().getEmail())
                .nome(prestador.getNome())
                .genero(prestador.getGenero())
                .dataNascimento(prestador.getDataNascimento())
                .descricao(prestador.getDescricao())
                .servicosPrestados(prestador.getServicosPrestados().toArray(new Boolean[0]))
                .precos(prestador.getPrecos().toArray(new BigDecimal[0]))
                .endereco(EnderecoDto.fromEndereco(prestador.getEndereco()))
                .imgPerfil(prestador.getImgPerfil())
                .contaBancaria(ContaBancariaDto.fromConta(prestador.getContaBancaria()))
                .servicos(prestador.getServicos().stream().map(ResumoServicoDto::fromServico).toArray(ResumoServicoDto[]::new))
                .avaliacao(prestadorService.calculateNotaMedia(prestador))
                .comentarios(prestadorService.getComentarios(prestador).toArray(new String[0]))
                .build();
    }

}
