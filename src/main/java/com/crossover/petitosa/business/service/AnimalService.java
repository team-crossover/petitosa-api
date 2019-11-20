package com.crossover.petitosa.business.service;

import com.crossover.petitosa.business.entity.Animal;
import com.crossover.petitosa.business.entity.Contratante;
import com.crossover.petitosa.data.repository.AnimalRepository;
import com.crossover.petitosa.presentation.dto.AnimalDto;
import com.crossover.petitosa.presentation.dto.NovoAnimalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@Transactional
public class AnimalService extends CrudService<Animal, Long, AnimalRepository> {

    @Autowired
    private ContratanteService contratanteService;

    @Autowired
    private AnimalService animalService;

    public Collection<Animal> findAllByIdDono(Long idDono) {
        if (idDono == null || contratanteService.findById(idDono) == null)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "ID de contratante não encontrado");
        return getRepository().findAllByDonoId(idDono);
    }

    public AnimalDto add(Long idContratante, NovoAnimalDto novoAnimalDto) {
        Contratante dono = idContratante == null ? null : contratanteService.findById(idContratante);
        if (dono == null)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "ID de contratante não encontrado");

        Animal animal = Animal.builder()
                .dono(dono)
                .especie(novoAnimalDto.getEspecie())
                .porte(novoAnimalDto.getPorte())
                .raca(novoAnimalDto.getRaca())
                .apelido(novoAnimalDto.getApelido())
                .observacoes(novoAnimalDto.getObservacoes())
                .imgAnimal(novoAnimalDto.getImgAnimal())
                .dataNascimento(novoAnimalDto.getDataNascimento())
                .build();
        animal = animalService.save(animal);

        return AnimalDto.fromAnimal(animal);
    }

    public AnimalDto update(Long id, NovoAnimalDto novosDadosDto) {
        Animal animal = findById(id);
        if (animal == null)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "ID de animal não encontrado");

        // Atualiza dados (imagem apenas se presente)
        animal.setApelido(novosDadosDto.getApelido());
        animal.setDataNascimento(novosDadosDto.getDataNascimento());
        animal.setEspecie(novosDadosDto.getEspecie());
        animal.setRaca(novosDadosDto.getRaca());
        animal.setPorte(novosDadosDto.getPorte());
        animal.setObservacoes(novosDadosDto.getObservacoes());
        if (animal.getImgAnimal() != null && !novosDadosDto.getImgAnimal().isEmpty())
            animal.setImgAnimal(novosDadosDto.getImgAnimal());
        animal = save(animal);

        return AnimalDto.fromAnimal(animal);
    }

    public boolean delete(Long id) {
        if (!existsById(id))
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "ID de animal não encontrado");

        deleteById(id);
        return true;
    }
}
