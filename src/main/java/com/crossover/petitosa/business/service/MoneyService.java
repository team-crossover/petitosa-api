package com.crossover.petitosa.business.service;

import com.crossover.petitosa.business.entity.CartaoCredito;
import com.crossover.petitosa.business.entity.ContaBancaria;
import com.crossover.petitosa.business.entity.Servico;
import com.crossover.petitosa.business.network.PoderosasBankNetwork;
import com.crossover.petitosa.presentation.dto.CartaoCreditoDto;
import com.crossover.petitosa.presentation.dto.ContaBancariaDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Log4j2
@Service
@Transactional
public class MoneyService {

    @Autowired
    private CartaoCreditoService cartaoCreditoService;

    @Autowired
    private ContaBancariaService contaBancariaService;

    @Autowired
    private PoderosasBankNetwork poderosasBankNetwork;

    @Autowired
    private ServicoService servicoService;

    public void validarConta(ContaBancariaDto conta) {
        if (!poderosasBankNetwork.validarConta(conta))
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Conta inválida");
    }

    public void validarCartao(CartaoCreditoDto cartao) {
        if (!poderosasBankNetwork.validarCartao(cartao))
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Cartão inválido");
    }

    public void processarPagamentoServico(Servico servico) {
        CartaoCredito cartaoContratante = servico.getContratante().getCartaoCredito();
        ContaBancaria contaPrestador = servico.getPrestador().getContaBancaria();
        ContaBancaria contaPetitosa = contaBancariaService.getContaPetitosa();

        // Paga o valor total para o petitosa
        poderosasBankNetwork.pagar(servico.getValorTotal(), cartaoContratante, contaPetitosa);
        log.info(servico.getContratante().getNome() + " pagou " + servico.getValorTotal() + " reais para o Petitosa");

        // Transfere o valor sem a taxa para o prestador
        poderosasBankNetwork.transferir(servico.getValorSemTaxa(), contaPetitosa, contaPrestador);
        BigDecimal taxa = servico.getValorTotal().subtract(servico.getValorSemTaxa());
        log.info("Petitosa repassou " + servico.getValorSemTaxa() + " reais para o prestador "
                + servico.getPrestador().getNome() + " e ficou com a taxa de " + taxa.setScale(2, RoundingMode.HALF_EVEN) + " reais");
    }
}
