package com.crossover.petitosa.presentation.dto;

import com.crossover.petitosa.business.entity.Endereco;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoDto {

    private Long id;

    @ApiModelProperty(example = "GO")
    private String uf;

    @ApiModelProperty(example = "Goiânia")
    private String cidade;

    @ApiModelProperty(example = "Setor Alaska")
    private String bairro;

    @NotBlank
    @ApiModelProperty(example = "Rua C 95")
    private String logradouro;

    @NotNull
    @Min(0)
    @Max(99999999)
    @ApiModelProperty(example = "74303360")
    private Integer cep;

    @ApiModelProperty(example = "48.5")
    private Double latitude;

    @ApiModelProperty(example = "-50.7")
    private Double longitude;

    public static EnderecoDto fromEndereco(Endereco endereco) {
        return EnderecoDto.builder()
                .id(endereco.getId())
                .uf(endereco.getUf())
                .cidade(endereco.getCidade())
                .bairro(endereco.getBairro())
                .logradouro(endereco.getLogradouro())
                .cep(endereco.getCep())
                .latitude(endereco.getLatitude())
                .longitude(endereco.getLongitude())
                .build();
    }
}
