package com.crossover.petitosa.business.service;

import com.crossover.petitosa.business.entity.*;
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

    public static final BigDecimal TAXA_PETITOSA = new BigDecimal("0.25");

    public static final BigDecimal TAXA_DESISTENCIA = new BigDecimal("8.00");

    @Autowired
    private ContaBancariaService contaBancariaService;

    @Autowired
    private PoderosasBankNetwork poderosasBankNetwork;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PrestadorService prestadorService;

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

    public Servico processarPagamentoServico(Servico servico) {
        CartaoCredito cartaoContratante = servico.getContratante().getCartaoCredito();
        ContaBancaria contaPrestador = servico.getPrestador().getContaBancaria();
        ContaBancaria contaPetitosa = contaBancariaService.getContaPetitosa();

        // Contratante paga o valor total para o petitosa
        poderosasBankNetwork.pagar(servico.getPrecoTotal(), cartaoContratante, contaPetitosa);
        log.info("Contratante" + servico.getContratante().getNome() + " pagou serviço de " + servico.getPrecoTotal() + " reais para o Petitosa");

        // Deduz taxa de desistência do valor recebido pago ao prestador
        BigDecimal valorPagoAoPrestador = servico.getPrecoServico();
        Prestador prestador = servico.getPrestador();
        if (prestador.getTaxaDesistenciaAPagar().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal taxaDesistenciaPaga = prestador.getTaxaDesistenciaAPagar().min(valorPagoAoPrestador);
            BigDecimal taxaDesistenciaSobrando = prestador.getTaxaDesistenciaAPagar()
                    .subtract(taxaDesistenciaPaga)
                    .max(BigDecimal.ZERO)
                    .setScale(2, RoundingMode.HALF_EVEN);
            valorPagoAoPrestador = valorPagoAoPrestador
                    .subtract(taxaDesistenciaPaga)
                    .max(BigDecimal.ZERO)
                    .setScale(2, RoundingMode.HALF_EVEN);
            prestador.setTaxaDesistenciaAPagar(taxaDesistenciaSobrando);
            prestador = prestadorService.save(prestador);
            log.info("Prestador " + servico.getPrestador().getNome() + " deixou de ganhar " + taxaDesistenciaPaga + " devido à taxa de desistência");
        }

        // Transfere o valor de serviço para o prestador
        if (valorPagoAoPrestador.compareTo(BigDecimal.ZERO) > 0) {
            poderosasBankNetwork.transferir(valorPagoAoPrestador, contaPetitosa, contaPrestador);
            log.info("Petitosa repassou " + valorPagoAoPrestador + " reais para o prestador " + servico.getPrestador().getNome());
        }
        servico.setValorRecebidoPeloPrestador(valorPagoAoPrestador);
        servico = servicoService.save(servico);
        return servico;
    }

    public Servico processarDesistenciaServicoPorPrestador(Servico servico) {
        Prestador prestador = servico.getPrestador();

        BigDecimal taxa = TAXA_DESISTENCIA;
        servico.setTaxaDesistenciaAdicionadaAoPrestador(taxa);
        servico = servicoService.save(servico);

        BigDecimal novaTaxaAPagar = prestador.getTaxaDesistenciaAPagar().add(taxa).setScale(2, RoundingMode.HALF_EVEN);
        prestador.setTaxaDesistenciaAPagar(novaTaxaAPagar);
        prestador = prestadorService.save(prestador);

        return servico;
    }

    public Servico processarDesistenciaServicoPorContratante(Servico servico) {
        Contratante contratante = servico.getContratante();
        CartaoCredito cartaoContratante = contratante.getCartaoCredito();
        ContaBancaria contaPetitosa = contaBancariaService.getContaPetitosa();

        BigDecimal taxa = TAXA_DESISTENCIA;
        poderosasBankNetwork.pagar(taxa, cartaoContratante, contaPetitosa);
        log.info("Contratante" + contratante.getNome() + " pagou taxa de desistência de " + taxa + " reais para o Petitosa");

        servico.setTaxaDesistenciaPagaPeloContratante(taxa);
        servico = servicoService.save(servico);
        return servico;
    }
}
