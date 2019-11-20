package com.crossover.petitosa.presentation.dto;

import com.crossover.petitosa.business.entity.Animal;
import com.crossover.petitosa.business.entity.Contratante;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContratanteDto {

    @NotNull
    private Long id;

    @NotNull
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

    @NotNull
    private EnderecoDto endereco;

    private String imgPerfil;

    @NotNull
    private CartaoCreditoDto cartaoCredito;

    private List<Long> idsAnimais;

    public static ContratanteDto fromContratante(Contratante contratante) {
        return ContratanteDto.builder()
                .id(contratante.getId())
                .idUsuario(contratante.getUsuario().getId())
                .email(contratante.getUsuario().getEmail())
                .nome(contratante.getNome())
                .genero(contratante.getGenero())
                .dataNascimento(contratante.getDataNascimento())
                .endereco(EnderecoDto.fromEndereco(contratante.getEndereco()))
                .imgPerfil(contratante.getImgPerfil())
                .cartaoCredito(CartaoCreditoDto.fromCartao(contratante.getCartaoCredito()))
                .idsAnimais(contratante.getAnimais().stream().map(Animal::getId).collect(Collectors.toList()))
                .build();
    }

}
