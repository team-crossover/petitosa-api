package com.crossover.petitosa.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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

    // -----------

    @JsonIgnore
    @OneToOne(mappedBy = "usuario")
    private Prestador prestador;

    @JsonIgnore
    @OneToOne(mappedBy = "usuario")
    private Contratante contratante;

}
