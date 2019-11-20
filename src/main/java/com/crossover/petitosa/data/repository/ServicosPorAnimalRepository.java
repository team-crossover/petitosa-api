package com.crossover.petitosa.data.repository;

import com.crossover.petitosa.business.entity.ServicosPorAnimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicosPorAnimalRepository extends JpaRepository<ServicosPorAnimal, Long> {

}