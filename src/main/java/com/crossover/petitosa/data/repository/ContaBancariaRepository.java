package com.crossover.petitosa.data.repository;

import com.crossover.petitosa.business.entity.ContaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {

}