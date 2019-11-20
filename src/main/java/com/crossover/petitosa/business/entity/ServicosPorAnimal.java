package com.crossover.petitosa.business.entity;

import com.crossover.petitosa.business.enums.TipoServico;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {})
@Entity
@Table(name = "servicosporanimais")
public class ServicosPorAnimal {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private Servico servico;

    @NotNull
    @ManyToOne
    private Animal animal;

    @Builder.Default
    @NotNull
    @Size(min = 1)
    @ElementCollection
    private List<TipoServico> tiposServico = new ArrayList<>();

}
