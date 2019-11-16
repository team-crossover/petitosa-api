package com.crossover.petitosa.presentation.controller;

import com.crossover.petitosa.business.service.EnderecoService;
import com.crossover.petitosa.presentation.dto.EnderecoDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @GetMapping("/api/v1/endereco")
    @ApiOperation("Obter endereço por CEP")
    private EnderecoDto getEnderecoByCEP(@ApiParam(example = "74303360") @RequestParam(name = "cep", required = true) Integer cep) {
        return EnderecoDto.fromEndereco(enderecoService.findEnderecoByCep(cep));
    }
}
