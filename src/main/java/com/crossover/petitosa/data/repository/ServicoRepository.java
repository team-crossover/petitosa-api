package com.crossover.petitosa.data.repository;

import com.crossover.petitosa.business.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findAllByContratanteId(Long idContratante);

    List<Servico> findAllByPrestadorId(Long idPrestador);
}