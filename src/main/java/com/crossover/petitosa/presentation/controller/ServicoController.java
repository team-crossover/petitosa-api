package com.crossover.petitosa.presentation.controller;

import com.crossover.petitosa.business.service.ServicoService;
import com.crossover.petitosa.presentation.dto.FiltroServicoDto;
import com.crossover.petitosa.presentation.dto.PrestadorEncontradoDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @PostMapping("/api/v1/servicos")
    @ApiOperation("Buscar prestadores para serviço")
    private List<PrestadorEncontradoDto> buscar(@RequestBody @Valid FiltroServicoDto filtroServicoDto) {
        return servicoService.buscarPrestadores(filtroServicoDto);
    }

}
