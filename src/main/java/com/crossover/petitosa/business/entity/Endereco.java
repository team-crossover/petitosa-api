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
@Table(name = "enderecos")
public class Endereco {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(min = 2, max = 2)
    private String uf;

    @NotBlank
    private String cidade;

    @NotBlank
    private String bairro;

    @NotBlank
    private String logradouro;

    @NotNull
    @Min(0)
    @Max(99999999)
    private Integer cep;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public Integer getCep() {
        return cep;
    }

    public void setCep(Integer cep) {
        this.cep = cep;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
