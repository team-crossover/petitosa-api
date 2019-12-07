package com.crossover.petitosa.business.service;

import com.crossover.petitosa.business.entity.CartaoCredito;
import com.crossover.petitosa.business.entity.ContaBancaria;
import com.crossover.petitosa.business.entity.Servico;
import com.crossover.petitosa.business.entity.Usuario;
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
    private ContaBancariaService contaBancariaService;

    @Autowired
    private PoderosasBankNetwork poderosasBankNetwork;

    @Autowired
    private UsuarioService usuarioService;

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
        poderosasBankNetwork.pagar(servico.getPrecoTotal(), cartaoContratante, contaPetitosa);
        log.info("Contratante" + servico.getContratante().getNome() + " pagou " + servico.getPrecoTotal() + " reais para o Petitosa");

        // Se o contratante pagou taxa de desistência, desconta ela da 'taxa a pagar' do contratante
        if (servico.getPrecoTaxaDesistencia().compareTo(BigDecimal.ZERO) > 0) {
            Usuario usuarioContratante = servico.getContratante().getUsuario();
            BigDecimal taxaDesistenciaSobrando = usuarioContratante.getTaxaDesistenciaAPagar()
                    .subtract(servico.getPrecoTaxaDesistencia())
                    .max(BigDecimal.ZERO)
                    .setScale(2, RoundingMode.HALF_EVEN);
            usuarioContratante.setTaxaDesistenciaAPagar(taxaDesistenciaSobrando);
            usuarioContratante = usuarioService.save(usuarioContratante);
            log.info("Contratante " + servico.getContratante().getNome() + " pagou " + servico.getPrecoTaxaDesistencia() + " como taxa de desistência");
        }

        BigDecimal valorServico = servico.getPrecoServico();

        // Se o prestador possui taxa de desistência, desconta ela do valor de serviço
        Usuario usuarioPrestador = servico.getPrestador().getUsuario();
        if (usuarioPrestador.getTaxaDesistenciaAPagar().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal taxaDesistenciaPaga = usuarioPrestador.getTaxaDesistenciaAPagar().min(valorServico);
            BigDecimal taxaDesistenciaSobrando = usuarioPrestador.getTaxaDesistenciaAPagar()
                    .subtract(taxaDesistenciaPaga)
                    .max(BigDecimal.ZERO)
                    .setScale(2, RoundingMode.HALF_EVEN);
            valorServico = valorServico
                    .subtract(taxaDesistenciaPaga)
                    .max(BigDecimal.ZERO)
                    .setScale(2, RoundingMode.HALF_EVEN);
            usuarioPrestador.setTaxaDesistenciaAPagar(taxaDesistenciaSobrando);
            usuarioPrestador = usuarioService.save(usuarioPrestador);
            log.info("Prestador " + servico.getPrestador().getNome() + " deixou de ganhar " + taxaDesistenciaPaga + " devido à taxa de desistência");
        }

        // Transfere o valor de serviço para o prestador
        if (valorServico.compareTo(BigDecimal.ZERO) > 0) {
            poderosasBankNetwork.transferir(valorServico, contaPetitosa, contaPrestador);
            log.info("Petitosa repassou " + valorServico + " reais para o prestador " + servico.getPrestador().getNome());
        }
    }
}
