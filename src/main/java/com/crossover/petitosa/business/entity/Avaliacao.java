package com.crossover.petitosa.business.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    private LocalDateTime data;

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
