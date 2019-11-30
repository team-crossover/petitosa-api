package com.crossover.petitosa.presentation.controller;

import com.crossover.petitosa.business.service.MoneyService;
import com.crossover.petitosa.presentation.dto.CartaoCreditoDto;
import com.crossover.petitosa.presentation.dto.ContaBancariaDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MoneyController {

    @Autowired
    private MoneyService moneyService;

    @PostMapping("/api/v1/money/conta/validar")
    @ApiOperation("Verifica os dados de conta bancária são válidos")
    private String validar(@RequestBody @Valid ContaBancariaDto contaBancariaDto) {
        moneyService.validarConta(contaBancariaDto);
        return "VÁLIDO";
    }

    @PostMapping("/api/v1/money/cartao/validar")
    @ApiOperation("Verifica os dados de cartão de crédito são válidos")
    private String validar(@RequestBody @Valid CartaoCreditoDto cartaoCreditoDto) {
        moneyService.validarCartao(cartaoCreditoDto);
        return "VÁLIDO";
    }
}
