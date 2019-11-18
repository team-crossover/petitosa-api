package com.crossover.petitosa.presentation.dto;

import com.crossover.petitosa.business.entity.Usuario;
import com.crossover.petitosa.business.enums.RoleUsuario;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto {

    @NotNull
    private Long id;

    @NotBlank
    @ApiModelProperty(example = "jonas@exemplo.com")
    private String email;

    private Long idContratante;

    private Long idPrestador;

    @NotNull
    @ApiModelProperty(example = "CONTRATANTE", allowableValues = "CONTRATANTE, PRESTADOR")
    private RoleUsuario role;

    public static UsuarioDto fromUsuario(Usuario usuario) {
        return UsuarioDto.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .idContratante(usuario.getContratante() == null ? null : usuario.getContratante().getId())
                .idPrestador(usuario.getPrestador() == null ? null : usuario.getPrestador().getId())
                .role(usuario.getRole())
                .build();
    }

}
