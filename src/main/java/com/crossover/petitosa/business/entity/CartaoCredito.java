package com.crossover.petitosa.business.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {})
@Entity
@Table(name = "cartoes")
public class CartaoCredito {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Pattern(regexp = "^\\d*$")
    @Size(min = 16, max = 16)
    private String numero;

    @NotBlank
    @Size(min = 7, max = 7)
    @Pattern(regexp = "^\\d\\d/\\d\\d\\d\\d*$") // MM/yyyy
    private String validade;

    @NotBlank
    @Pattern(regexp = "^\\d*$") // apenas números
    @Size(min = 3, max = 3)
    private String cvv;

}
