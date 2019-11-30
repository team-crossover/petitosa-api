package com.crossover.petitosa.business.service;

import com.crossover.petitosa.business.entity.ContaBancaria;
import com.crossover.petitosa.data.config.InitialDataLoader;
import com.crossover.petitosa.data.repository.ContaBancariaRepository;
import com.crossover.petitosa.presentation.dto.ContaBancariaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ContaBancariaService extends CrudService<ContaBancaria, Long, ContaBancariaRepository> {

    @Autowired
    private InitialDataLoader initialDataLoader;

    public ContaBancaria getContaPetitosa() {
        return initialDataLoader.getContaPetitosa();
    }

}
