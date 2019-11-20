package com.crossover.petitosa.presentation.controller;

import com.crossover.petitosa.business.entity.Prestador;
import com.crossover.petitosa.business.service.PrestadorService;
import com.crossover.petitosa.presentation.dto.NovoPrestadorDto;
import com.crossover.petitosa.presentation.dto.PrestadorDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PrestadorController {

    @Autowired
    private PrestadorService prestadorService;

    @PostMapping("/api/v1/prestadores")
    @ApiOperation("Cadastrar novo prestador")
    private PrestadorDto add(@RequestBody @Valid NovoPrestadorDto novoPrestadorDto) {
        return prestadorService.add(novoPrestadorDto);
    }

    @GetMapping("/api/v1/prestadores")
    @ApiOperation("Obtém dados de todos prestadores")
    private List<PrestadorDto> getAll() {
        return prestadorService.findAll().stream().map(PrestadorDto::fromPrestador).collect(Collectors.toList());
    }

    @PostMapping("/api/v1/prestador/{id}")
    @ApiOperation("Editar dados de prestador")
    private PrestadorDto update(
            @ApiParam("ID do prestador a ser atualizado") @PathVariable(value = "id", required = true) Long id,
            @ApiParam("Novos dados do prestador") @RequestBody @Valid NovoPrestadorDto novosDadosDto) {
        return prestadorService.update(id, novosDadosDto);
    }

    @GetMapping("/api/v1/prestador/{id}")
    @ApiOperation("Obtém dados de prestador")
    private PrestadorDto getById(@ApiParam("ID do prestador") @PathVariable(value = "id", required = true) Long id) {
        Prestador prestador = prestadorService.findById(id);
        if (prestador == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        return PrestadorDto.fromPrestador(prestador);
    }
}
