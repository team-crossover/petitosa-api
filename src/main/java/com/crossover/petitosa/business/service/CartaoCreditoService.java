package com.crossover.petitosa.business.service;

import com.crossover.petitosa.business.entity.CartaoCredito;
import com.crossover.petitosa.data.repository.CartaoCreditoRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CartaoCreditoService extends CrudService<CartaoCredito, Long, CartaoCreditoRepository> {

}
