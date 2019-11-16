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

    @ApiModelProperty(example = "123456", notes = "Obrigatório ao cadastrar novo prestador, opcional ao editar prestador existente")
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
    @ApiModelProperty(example = "Adoro animais")
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
    @ApiModelProperty(example = "[20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20]")
    private Double[] precos = new Double[15];

}
