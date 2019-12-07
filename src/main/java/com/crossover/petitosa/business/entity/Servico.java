package com.crossover.petitosa.business.entity;

import com.crossover.petitosa.business.enums.StatusServico;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"servicosPorAnimais"})
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

    @NotNull
    @OneToOne(orphanRemoval = true)
    private Endereco enderecoServico;

    @Size(max = 500)
    private String observacoes;

    private BigDecimal precoServico;

    private BigDecimal precoTaxaPetitosa;

    private BigDecimal precoTaxaDesistencia;

    private BigDecimal precoTotal;

    private BigDecimal valorRecebidoPeloPrestador;

    private BigDecimal taxaDesistenciaPagaPeloContratante;

    private BigDecimal taxaDesistenciaAdicionadaAoPrestador;

    @NotNull
    private LocalDateTime dataSolicitacao;

    private LocalDateTime dataAceitacao;

    private LocalDateTime dataRejeicao;

    private LocalDateTime dataDesistencia;

    @NotNull
    private LocalDateTime dataEsperadaRealizacao;

    private LocalDateTime dataInicioRealizacao;

    private LocalDateTime dataTerminoRealizacao;

    @NotNull
    private StatusServico status;

    @OneToOne(orphanRemoval = true)
    private Avaliacao avaliacao;

    // -----

    @Builder.Default
    @OneToMany(mappedBy = "servico", orphanRemoval = true)
    private List<ServicosPorAnimal> servicosPorAnimais = new ArrayList<>();

}
