package com.crossover.petitosa.presentation.dto;

import com.crossover.petitosa.business.entity.Prestador;
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
    @ApiModelProperty(example = "Adoro animais")
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
    private Double[] precos = new Double[15];

    public static PrestadorDto fromPrestador(Prestador prestador) {
        return PrestadorDto.builder()
                .id(prestador.getId())
                .idUsuario(prestador.getUsuario().getId())
                .email(prestador.getUsuario().getEmail())
                .nome(prestador.getNome())
                .genero(prestador.getGenero())
                .dataNascimento(prestador.getDataNascimento())
                .descricao(prestador.getDescricao())
                .servicosPrestados(prestador.getServicosPrestados().toArray(new Boolean[0]))
                .precos(prestador.getPrecos().toArray(new Double[0]))
                .endereco(EnderecoDto.fromEndereco(prestador.getEndereco()))
                .build();
    }

}
