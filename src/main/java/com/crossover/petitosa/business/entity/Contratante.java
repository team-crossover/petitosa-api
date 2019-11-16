package com.crossover.petitosa.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"avaliacoes", "comentarios"})
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

    // -----------

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "avaliador")
    private List<Avaliacao> avaliacoes = new ArrayList<>();

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "avaliador")
    private List<Comentario> comentarios = new ArrayList<>();


}
