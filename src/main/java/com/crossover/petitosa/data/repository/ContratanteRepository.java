package com.crossover.petitosa.data.repository;

import com.crossover.petitosa.business.entity.Contratante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratanteRepository extends JpaRepository<Contratante, Long> {

}