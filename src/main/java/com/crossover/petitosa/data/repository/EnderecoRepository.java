package com.crossover.petitosa.data.repository;

import com.crossover.petitosa.business.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

}