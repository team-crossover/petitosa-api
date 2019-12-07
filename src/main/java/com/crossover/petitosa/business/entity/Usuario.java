package com.crossover.petitosa.business.entity;

import com.crossover.petitosa.business.enums.RoleUsuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"prestador", "contratante"})
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Column(unique = true)
    @Email
    private String email;

    @NotBlank
    private String senha;

    @NotNull
    private RoleUsuario role;

    @Builder.Default
    @NotNull
    private BigDecimal taxaDesistenciaAPagar = BigDecimal.ZERO;

    // -----------

    @JsonIgnore
    @OneToOne(mappedBy = "usuario")
    private Prestador prestador;

    @JsonIgnore
    @OneToOne(mappedBy = "usuario")
    private Contratante contratante;

}
