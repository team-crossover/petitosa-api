package com.crossover.petitosa.business.entity;

import com.crossover.petitosa.business.enums.StatusServico;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {})
@Entity
@Table(name = "servicos")
public class Servico {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private Contratante contratante;

    @NotNull
    @ManyToOne
    private Prestador prestador;

    @Builder.Default
    @OneToMany(mappedBy = "servico", orphanRemoval = true)
    private List<ServicosPorAnimal> servicosPorAnimais = new ArrayList<>();

    @NotNull
    private LocalDateTime dataAgendado;

    @NotNull
    @OneToOne(orphanRemoval = true)
    private Endereco enderecoAgendado;

    @Size(max = 500)
    private String observacoes;

    @NotNull
    @Min(0)
    private Double valorTotal;

    @NotNull
    private LocalDateTime dataSolicitado;

    @NotNull
    private StatusServico status;

}
