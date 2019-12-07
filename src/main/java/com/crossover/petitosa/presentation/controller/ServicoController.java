package com.crossover.petitosa.presentation.controller;

import com.crossover.petitosa.business.entity.Servico;
import com.crossover.petitosa.business.service.ServicoService;
import com.crossover.petitosa.presentation.dto.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @PostMapping("/api/v1/servicos/buscar")
    @ApiOperation("Buscar prestadores para serviço")
    private List<PrestadorEncontradoDto> buscar(@RequestBody @Valid FiltroServicoDto filtroServicoDto) {
        return servicoService.buscarPrestadores(filtroServicoDto);
    }

    @PostMapping("/api/v1/servicos/solicitar")
    @ApiOperation("Solicita um novo serviço")
    private ServicoDto solicitar(@RequestBody @Valid SolicitacaoServicoDto solicitacaoServicoDto) {
        return servicoService.solicitar(solicitacaoServicoDto);
    }

    @GetMapping("/api/v1/servicos/{idServico}")
    @ApiOperation("Obter dados de serviço")
    private ServicoDto getById(@PathVariable(value = "idServico") Long idServico) {
        Servico servico = servicoService.findById(idServico);
        if (servico == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID de servico nao encontrado");
        return ServicoDto.fromServico(servico);
    }

    @GetMapping("/api/v1/servicos/{idServico}/aceitar")
    @ApiOperation("Aceita um serviço pendente e o retorna")
    private ServicoDto aceitar(@PathVariable("idServico") Long idServico) {
        return servicoService.aceitar(idServico);
    }

    @GetMapping("/api/v1/servicos/{idServico}/rejeitar")
    @ApiOperation("Rejeita um serviço pendente e o retorna")
    private ServicoDto rejeitar(@PathVariable("idServico") Long idServico) {
        return servicoService.rejeitar(idServico);
    }

    @GetMapping("/api/v1/servicos/{idServico}/desistir")
    @ApiOperation("Desiste de um serviço pendente ou aceito e o retorna")
    private ServicoDto desistir(@PathVariable("idServico") Long idServico,
                                @RequestParam(name = "idUsuario", required = true) Integer idUsuario) {
        return servicoService.desistir(idUsuario, idServico);
    }

    @GetMapping("/api/v1/servicos/{idServico}/iniciar")
    @ApiOperation("Indica o início da realização de um serviço aceito e o retorna")
    private ServicoDto iniciarRealizacao(@PathVariable("idServico") Long idServico) {
        return servicoService.iniciarRealizacao(idServico);
    }

    @GetMapping("/api/v1/servicos/{idServico}/terminar")
    @ApiOperation("Indica o término da realização de um serviço em andamento, finalizando a transação bancária e retornando o serviço")
    private ServicoDto terminarRealizacao(@PathVariable("idServico") Long idServico) {
        return servicoService.terminarRealizacao(idServico);
    }

    @PostMapping("/api/v1/servicos/{idServico}/avaliar")
    @ApiOperation("Avalia um serviço terminado e o retorna")
    private ServicoDto avaliar(@PathVariable("idServico") Long idServico,
                               @RequestBody @Valid NovaAvaliacaoDto avaliacaoDto) {
        return servicoService.avaliar(idServico, avaliacaoDto);
    }

    @GetMapping("/api/v1/servicos/by-prestador/{idPrestador}")
    @ApiOperation("Lista serviços de um prestador, retornando-os separados por status")
    private ServicosPorStatusDto listarPorPrestador(@PathVariable("idPrestador") Long idPrestador) {
        return ServicosPorStatusDto.fromServicos(servicoService.listarServicosPorPrestador(idPrestador));
    }

    @GetMapping("/api/v1/servicos/by-contratante/{idContratante}")
    @ApiOperation("Buscar serviços de um contratante, retornando-os separados por status")
    private ServicosPorStatusDto listarPorContratante(@PathVariable("idContratante") Long idContratante) {
        return ServicosPorStatusDto.fromServicos(servicoService.listarServicosPorContratante(idContratante));
    }
}
