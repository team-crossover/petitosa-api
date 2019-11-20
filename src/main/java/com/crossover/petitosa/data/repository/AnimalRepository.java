package com.crossover.petitosa.data.repository;

import com.crossover.petitosa.business.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    Collection<Animal> findAllByDonoId(Long idDono);

}