package com.crossover.petitosa.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"servicos"})
@Entity
@Table(name = "prestadores")
public class Prestador {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Usuario usuario;

    @NotBlank
    private String nome;

    private String genero;

    private LocalDate dataNascimento;

    @Size(max = 1000)
    private String descricao;

    @NotNull
    @OneToOne(orphanRemoval = true)
    private Endereco endereco;

    @NotNull
    @Size(min = 15, max = 15)
    @ElementCollection
    @Builder.Default
    private List<Boolean> servicosPrestados = new ArrayList<>();

    @Builder.Default
    @NotNull
    @Size(min = 15, max = 15)
    @ElementCollection
    private List<BigDecimal> precos = new ArrayList<>();

    @Basic(fetch = FetchType.LAZY)
    @Size(max = 10485760) // 10 MB
    private String imgPerfil;

    @NotNull
    @OneToOne(orphanRemoval = true)
    private ContaBancaria contaBancaria;

    @Builder.Default
    @NotNull
    private BigDecimal taxaDesistenciaAPagar = BigDecimal.ZERO;

    // ------------

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "prestador")
    private List<Servico> servicos = new ArrayList<>();

    // ÍNDICES DAS ARRAYS DE SERVIÇOS PRESTADOS E PREÇOS
    // i    espécie/porte/tipo de serviço
    // ----------------------------------
    // 0    cão p banho
    // 1    cão p tosa
    // 2    cão p passeio
    // 3    cão m banho
    // 4    cão m tosa
    // 5    cão m passeio
    // 6    cão g banho
    // 7    cão g tosa
    // 8    cão g passeio
    // 9    gato p banho
    // 10   gato p tosa
    // 11   gato m banho
    // 12   gato m tosa
    // 13   gato g banho
    // 14   gato g tosa
}
