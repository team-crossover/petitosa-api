package com.crossover.petitosa.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NovoContratanteDto {

    @NotBlank
    @Email
    @ApiModelProperty(example = "jonas@exemplo.com")
    private String email;

    @ApiModelProperty(example = "123456", notes = "Obrigatório ao add novo contratante, opcional ao update contratante existente")
    private String senha;

    @NotBlank
    @ApiModelProperty(example = "Jonas")
    private String nome;

    @ApiModelProperty(example = "M")
    private String genero;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @ApiModelProperty(example = "31/12/1998")
    private LocalDate dataNascimento;

    @NotNull
    private NovoEnderecoDto endereco;

    @ApiModelProperty(notes = "Opcional, se tiver em branco será ignorado")
    @Size(max = 10485760) // 10 MB
    private String imgPerfil;

    @NotNull
    private CartaoCreditoDto cartaoCredito;

}
