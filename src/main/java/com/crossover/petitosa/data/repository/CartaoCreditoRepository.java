package com.crossover.petitosa.data.repository;

import com.crossover.petitosa.business.entity.CartaoCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartaoCreditoRepository extends JpaRepository<CartaoCredito, Long> {

}