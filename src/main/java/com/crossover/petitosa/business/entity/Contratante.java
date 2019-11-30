package com.crossover.petitosa.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"servicos", "animais"})
@Entity
@Table(name = "contratantes")
public class Contratante {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @OneToOne
    private Usuario usuario;

    @NotBlank
    private String nome;

    private String genero;

    private LocalDate dataNascimento;

    @NotNull
    @OneToOne(orphanRemoval = true)
    private Endereco endereco;

    @Basic(fetch = FetchType.LAZY)
    @Size(max = 10485760) // 10 MB
    private String imgPerfil;

    @NotNull
    @OneToOne(orphanRemoval = true)
    private CartaoCredito cartaoCredito;

    // -----------

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "dono")
    private List<Animal> animais = new ArrayList<>();

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "contratante")
    private List<Servico> servicos = new ArrayList<>();

}
