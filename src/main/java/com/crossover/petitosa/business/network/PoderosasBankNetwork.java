package com.crossover.petitosa.business.network;

import com.crossover.petitosa.business.entity.CartaoCredito;
import com.crossover.petitosa.business.entity.ContaBancaria;
import com.crossover.petitosa.presentation.dto.CartaoCreditoDto;
import com.crossover.petitosa.presentation.dto.ContaBancariaDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Log4j2
@Component
public class PoderosasBankNetwork {

    private static final String PODEROSAS_BANK_URL = "http://poderosas-bank.herokuapp.com";

    private HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build());
    private RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);

    public ContaBancariaDto getContaByUsuario(String username, String senha) {
        LoginDto loginDto = LoginDto.builder()
                .username(username)
                .senha(senha)
                .build();
        String url = PODEROSAS_BANK_URL + "/api/v1/usuario/login";
        UsuarioBancoDto usuario = restTemplate.postForObject(url, loginDto, UsuarioBancoDto.class);
        if (usuario == null)
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível autenticar com usuario do banco");

        url = PODEROSAS_BANK_URL + "/api/v1/conta/" + usuario.idContaBancaria;
        return restTemplate.getForObject(url, ContaBancariaDto.class);
    }

    public boolean validarConta(ContaBancariaDto conta) {
        String url = PODEROSAS_BANK_URL + "/api/v1/contas/validar";
        ResponseEntity<ContaBancariaDto> response = restTemplate.postForEntity(url, conta, ContaBancariaDto.class);
        return response.getStatusCode() == HttpStatus.OK;
    }

    public boolean validarCartao(CartaoCreditoDto cartao) {
        String url = PODEROSAS_BANK_URL + "/api/v1/cartoes/validar";
        ResponseEntity<CartaoCreditoDto> response = restTemplate.postForEntity(url, cartao, CartaoCreditoDto.class);
        return response.getStatusCode() == HttpStatus.OK;
    }

    public ResultadoPagamentoDto pagar(BigDecimal valor, CartaoCredito cartaoPagador, ContaBancaria contaRecebedora) {
        String url = PODEROSAS_BANK_URL + "/api/v1/transacoes/pagar";
        PagarDto pagarDto = PagarDto.builder()
                .valor(valor)
                .cartaoPagante(CartaoCreditoDto.fromCartao(cartaoPagador))
                .contaRecebedora(ContaBancariaDto.fromConta(contaRecebedora))
                .build();
        return restTemplate.postForObject(url, pagarDto, ResultadoPagamentoDto.class);
    }

    public ResultadoTransferenciaDto transferir(BigDecimal valor, ContaBancaria contaPagante, ContaBancaria contaRecebedora) {
        String url = PODEROSAS_BANK_URL + "/api/v1/transacoes/transferir";
        TransferirDto transferirDto = TransferirDto.builder()
                .valor(valor)
                .contaPagante(ContaBancariaDto.fromConta(contaPagante))
                .contaRecebedora(ContaBancariaDto.fromConta(contaRecebedora))
                .build();
        return restTemplate.postForObject(url, transferirDto, ResultadoTransferenciaDto.class);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class LoginDto {
        public String username;
        public String senha;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ResultadoPagamentoDto {
        public BigDecimal totalPago;
        public BigDecimal novaFaturaPagante;
        public BigDecimal novoSaldoRecebedor;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ResultadoTransferenciaDto {
        public boolean realizado;
        public BigDecimal totalTransferido;
        public BigDecimal novoSaldoPagante;
        public BigDecimal novoSaldoRecebedor;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class UsuarioBancoDto {
        public Long id;
        public Long idContaBancaria;
        public String username;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class PagarDto {
        public BigDecimal valor;
        public CartaoCreditoDto cartaoPagante;
        public ContaBancariaDto contaRecebedora;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class TransferirDto {
        public BigDecimal valor;
        public ContaBancariaDto contaPagante;
        public ContaBancariaDto contaRecebedora;
    }
}
