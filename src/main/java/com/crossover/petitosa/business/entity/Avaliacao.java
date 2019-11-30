package com.crossover.petitosa.business.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"servico"})
@Entity
@Table(name = "avaliacoes")
public class Avaliacao {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Min(0)
    @Max(5)
    private Double nota;

    @Size(max = 240)
    private String texto;

    @NotNull
    private LocalDateTime dataAvaliacao;

    // -------------

    @NotNull
    @OneToOne(mappedBy = "avaliacao")
    private Servico servico;

}
