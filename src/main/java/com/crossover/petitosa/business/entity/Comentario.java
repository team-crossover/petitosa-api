package com.crossover.petitosa.business.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {})
@Entity
@Table(name = "comentarios")
public class Comentario {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private Contratante avaliador;

    @NotNull
    @ManyToOne
    private Prestador prestador;

    @NotBlank
    @Size(max = 500)
    private String texto;

}
