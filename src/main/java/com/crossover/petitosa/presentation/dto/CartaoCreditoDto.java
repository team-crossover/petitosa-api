package com.crossover.petitosa.presentation.dto;

import com.crossover.petitosa.business.entity.CartaoCredito;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {})
public class CartaoCreditoDto {

    private Long id;

    @ApiModelProperty(example = "5555666677778884")
    @NotBlank
    @Pattern(regexp = "^\\d*$")
    @Size(min = 16, max = 16)
    private String numero;

    @ApiModelProperty(example = "12/2022")
    @NotBlank
    @Size(min = 7, max = 7)
    @Pattern(regexp = "^\\d\\d/\\d\\d\\d\\d*$") // MM/yyyy
    private String validade;

    @ApiModelProperty(example = "123")
    @NotBlank
    @Pattern(regexp = "^\\d*$") // apenas números
    @Size(min = 3, max = 3)
    private String cvv;

    public static CartaoCreditoDto fromCartao(CartaoCredito cartaoCredito) {
        return CartaoCreditoDto.builder()
                .id(cartaoCredito.getId())
                .numero(cartaoCredito.getNumero())
                .validade(cartaoCredito.getValidade())
                .cvv(cartaoCredito.getCvv())
                .build();
    }
}
