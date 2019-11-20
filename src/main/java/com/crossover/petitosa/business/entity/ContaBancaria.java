package com.crossover.petitosa.business.entity;

import com.crossover.petitosa.business.enums.TipoConta;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {})
@Entity
@Table(name = "contas")
public class ContaBancaria {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private TipoConta tipo;

    @NotBlank
    @Pattern(regexp = "^\\d*$")
    @Size(min = 1)
    private String numero;

    @NotBlank
    @Pattern(regexp = "^\\d$")
    private String digito;

    @NotBlank
    @Pattern(regexp = "^\\d*$")
    @Size(min = 1)
    private String agencia;

}
