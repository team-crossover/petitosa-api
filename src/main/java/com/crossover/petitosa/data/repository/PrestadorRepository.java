package com.crossover.petitosa.data.repository;

import com.crossover.petitosa.business.entity.Prestador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestadorRepository extends JpaRepository<Prestador, Long> {

}