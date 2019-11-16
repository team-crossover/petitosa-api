package com.crossover.petitosa.business.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {})
@Entity
@Table(name = "avaliacoes")
public class Avaliacao {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private Contratante avaliador;

    @NotNull
    @ManyToOne
    private Prestador prestador;

    @NotNull
    @Min(0)
    @Max(5)
    private Double nota;

}
