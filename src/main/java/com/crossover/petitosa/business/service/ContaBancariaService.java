package com.crossover.petitosa.business.service;

import com.crossover.petitosa.business.entity.ContaBancaria;
import com.crossover.petitosa.data.repository.ContaBancariaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ContaBancariaService extends CrudService<ContaBancaria, Long, ContaBancariaRepository> {

}
