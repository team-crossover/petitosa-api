package com.crossover.petitosa.presentation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @NotBlank
    @ApiModelProperty(example = "jonas@exemplo.com")
    private String email;

    @NotBlank
    @ApiModelProperty(example = "123456")
    private String senha;

}
