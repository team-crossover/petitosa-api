package com.crossover.petitosa.presentation.controller;

import com.crossover.petitosa.business.entity.Animal;
import com.crossover.petitosa.business.service.AnimalService;
import com.crossover.petitosa.presentation.dto.AnimalDto;
import com.crossover.petitosa.presentation.dto.NovoAnimalDto;
import com.crossover.petitosa.presentation.dto.RespostaSimplesDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    @GetMapping("/api/v1/animais")
    @ApiOperation("Obtém dados de todos animais")
    private List<AnimalDto> getAll(
            @RequestParam(name = "idDono", required = false) Long idDono) {
        Collection<Animal> animais;
        if (idDono != null)
            animais = animalService.findAllByIdDono(idDono);
        else
            animais = animalService.findAll();
        return animais.stream().map(AnimalDto::fromAnimal).collect(Collectors.toList());
    }

    @PostMapping("/api/v1/animais")
    @ApiOperation("Cadastrar novo animal a um contratante")
    private AnimalDto add(
            @ApiParam("ID do contratante dono do animal") @RequestParam(name = "idDono", required = true) Long idDono,
            @ApiParam("Dados do animal") @RequestBody @Valid NovoAnimalDto novoAnimalDto) {
        return animalService.add(idDono, novoAnimalDto);
    }

    @GetMapping("/api/v1/animal/{id}")
    @ApiOperation("Obtém dados de animal")
    private AnimalDto getById(@ApiParam("ID do animal") @PathVariable(value = "id", required = true) Long id) {
        Animal animal = animalService.findById(id);
        if (animal == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        return AnimalDto.fromAnimal(animal);
    }

    @PostMapping("/api/v1/animal/{id}")
    @ApiOperation("Editar dados de animal")
    private AnimalDto update(
            @ApiParam("ID do animal a ser atualizado") @PathVariable(value = "id", required = true) Long id,
            @ApiParam("Novos dados do animal") @RequestBody @Valid NovoAnimalDto novosDadosDto) {
        return animalService.update(id, novosDadosDto);
    }

    @DeleteMapping("/api/v1/animal/{id}")
    @ApiOperation("Deleta animal")
    private RespostaSimplesDto delete(@ApiParam("ID do animal") @PathVariable(value = "id", required = true) Long id) {
        animalService.delete(id);
        return new RespostaSimplesDto(HttpStatus.OK, "/api/v1/animal/" + id, "Deletado com sucesso");
    }
}
