package com.crossover.petitosa.presentation.controller;

import com.crossover.petitosa.business.entity.Contratante;
import com.crossover.petitosa.business.service.ContratanteService;
import com.crossover.petitosa.presentation.dto.ContratanteDto;
import com.crossover.petitosa.presentation.dto.NovoContratanteDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;

@RestController
public class ContratanteController {

    @Autowired
    private ContratanteService contratanteService;

    @PostMapping("/api/v1/contratantes")
    @ApiOperation("Cadastrar novo contratante")
    private ContratanteDto add(@RequestBody @Valid NovoContratanteDto novoContratanteDto) {
        return contratanteService.add(novoContratanteDto);
    }

    @PostMapping("/api/v1/contratante/{id}")
    @ApiOperation("Editar dados de contratante")
    private ContratanteDto update(
            @ApiParam("ID do contratante a ser atualizado") @PathVariable(value = "id", required = true) Long id,
            @ApiParam("Novos dados do contratante") @RequestBody @Valid NovoContratanteDto novosDadosDto) {
        return contratanteService.update(id, novosDadosDto);
    }

    @GetMapping("/api/v1/contratante/{id}")
    @ApiOperation("Obtém dados de contratante")
    private ContratanteDto getById(@ApiParam("ID do contratante") @PathVariable(value = "id", required = true) Long id) {
        Contratante contratante = contratanteService.findById(id);
        if (contratante == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        return ContratanteDto.fromContratante(contratante);
    }
}
