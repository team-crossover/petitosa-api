package com.crossover.petitosa.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NovoPrestadorDto {

    @NotBlank
    @Email
    @ApiModelProperty(example = "jonas@exemplo.com")
    private String email;

    @ApiModelProperty(example = "123456", notes = "Obrigatório ao add novo prestador, opcional ao update prestador existente")
    private String senha;

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
    private NovoEnderecoDto endereco;

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
    @ApiModelProperty(example = "[20.5, 20.25, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20]")
    private BigDecimal[] precos = new BigDecimal[15];

    @ApiModelProperty(notes = "Opcional, se tiver em branco será ignorado")
    @Size(max = 10485760) // 10 MB
    private String imgPerfil;

    @NotNull
    private ContaBancariaDto contaBancaria;
}
