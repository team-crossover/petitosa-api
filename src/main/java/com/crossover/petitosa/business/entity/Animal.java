package com.crossover.petitosa.business.entity;

import com.crossover.petitosa.business.enums.EspecieAnimal;
import com.crossover.petitosa.business.enums.PorteAnimal;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {})
@Entity
@Table(name = "animais")
public class Animal {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private Contratante dono;

    @NotNull
    private EspecieAnimal especie;

    @NotNull
    private PorteAnimal porte;

    private String raca;

    @NotBlank
    private String apelido;

    @Size(max = 500)
    private String observacoes;

    private LocalDate dataNascimento;

    @Basic(fetch = FetchType.LAZY)
    @Size(max = 10485760) // 10 MB
    private String imgAnimal;

}
