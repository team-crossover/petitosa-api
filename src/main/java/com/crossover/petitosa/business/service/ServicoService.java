package com.crossover.petitosa.business.service;

import com.crossover.petitosa.business.entity.*;
import com.crossover.petitosa.business.enums.EspecieAnimal;
import com.crossover.petitosa.business.enums.PorteAnimal;
import com.crossover.petitosa.business.enums.TipoServico;
import com.crossover.petitosa.data.repository.ServicoRepository;
import com.crossover.petitosa.presentation.dto.FiltroServicoDto;
import com.crossover.petitosa.presentation.dto.PrestadorEncontradoDto;
import com.crossover.petitosa.presentation.dto.ServicosPorAnimalDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Log4j2
@Service
@Transactional
public class ServicoService extends CrudService<Servico, Long, ServicoRepository> {

    public static final double TAXA_PETITOSA = 0.25;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private AnimalService animalService;

    @Autowired
    private PrestadorService prestadorService;

    @Autowired
    private ContratanteService contratanteService;

    // TODO: solicitar servico by solicitacao servico

    public List<PrestadorEncontradoDto> buscarPrestadores(FiltroServicoDto filtroServicoDto) {

        Contratante contratante = filtroServicoDto.getIdContratante() == null ? null : contratanteService.findById(filtroServicoDto.getIdContratante());
        if (contratante == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID de contratante não encontrado");

        Collection<Prestador> prestadores = prestadorService.findAll();
        List<PrestadorEncontradoDto> prestadorEncontradoDtos = new ArrayList<>();
        for (Prestador p : prestadores) {
            // Evita prestadores que não suportam um dos serviços desejados
            if (!prestadorPrestaTodosServicos(p, filtroServicoDto.getServicosPorAnimais()))
                continue;

            double precoTotal = calcularPrecoTodosServicos(p, filtroServicoDto.getServicosPorAnimais());

            // Evita prestadores cujo preço total é acima do máximo
            if (filtroServicoDto.getPrecoTotalMaximo() != null && precoTotal > filtroServicoDto.getPrecoTotalMaximo())
                continue;

            double distancia = calcularDistanciaPrestador(contratante, p);

            // Evita prestadores cuja distância é acima da máxima
            if (filtroServicoDto.getDistanciaMaxima() != null && distancia > filtroServicoDto.getDistanciaMaxima())
                continue;

            // Cria o DTO de prestador encontrado
            prestadorEncontradoDtos.add(PrestadorEncontradoDto.builder()
                    .nome(p.getNome())
                    .descricao(p.getDescricao())
                    .avaliacao(prestadorService.calculateAvaliacaoMedia(p))
                    .distancia(distancia)
                    .idPrestador(p.getId())
                    .imgPerfil(p.getImgPerfil())
                    .precoTotal(precoTotal)
                    .build());
        }
        return prestadorEncontradoDtos;
    }

    private double calcularPrecoTodosServicos(Prestador prestador, ServicosPorAnimalDto[] servicos) {
        double sum = 0.0;
        for (ServicosPorAnimalDto servicosPorAnimalDto : servicos)
            sum += calcularPrecoServicosPorAnimal(prestador, servicosPorAnimalDto);
        sum += sum * TAXA_PETITOSA;
        return sum;
    }

    private double calcularPrecoServicosPorAnimal(Prestador prestador, ServicosPorAnimalDto servicosPorAnimalDto) {
        Animal animal = animalService.findById(servicosPorAnimalDto.getIdAnimal());
        if (animal == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID de animal não encontrado");

        double sum = 0.0;
        List<Double> precos = prestador.getPrecos();
        for (TipoServico tipoServico : servicosPorAnimalDto.getTiposServicos()) {
            int indice = getIndiceArrayDeServicos(animal.getEspecie(), animal.getPorte(), tipoServico);
            sum += precos.get(indice);
        }
        return sum;
    }

    private double calcularDistanciaPrestador(Contratante contratante, Prestador prestador) {
        Endereco endereco1 = contratante.getEndereco();
        Endereco endereco2 = prestador.getEndereco();
        return enderecoService.calculateDistanceInMeters(endereco1, endereco2);
    }

    private boolean prestadorPrestaTodosServicos(Prestador prestador, ServicosPorAnimalDto[] servicos) {
        for (ServicosPorAnimalDto servicosPorAnimalDto : servicos)
            if (!prestadorPrestaServicosPorAnimal(prestador, servicosPorAnimalDto))
                return false;
        return true;
    }

    private boolean prestadorPrestaServicosPorAnimal(Prestador prestador, ServicosPorAnimalDto servicosPorAnimalDto) {
        Animal animal = animalService.findById(servicosPorAnimalDto.getIdAnimal());
        if (animal == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID de animal não encontrado");

        List<Boolean> servicosPrestados = prestador.getServicosPrestados();
        for (TipoServico tipoServico : servicosPorAnimalDto.getTiposServicos()) {
            int indice = getIndiceArrayDeServicos(animal.getEspecie(), animal.getPorte(), tipoServico);
            if (!servicosPrestados.get(indice))
                return false;
        }
        return true;
    }

    private int getIndiceArrayDeServicos(EspecieAnimal especie, PorteAnimal porte, TipoServico tipoServico) {
        // 0    cão p banho
        // 1    cão p tosa
        // 2    cão p passeio
        // 3    cão m banho
        // 4    cão m tosa
        // 5    cão m passeio
        // 6    cão g banho
        // 7    cão g tosa
        // 8    cão g passeio
        // 9    gato p banho
        // 10   gato p tosa
        // 11   gato m banho
        // 12   gato m tosa
        // 13   gato g banho
        // 14   gato g tosa

        int indiceBase = 0;
        switch (especie) {
            case CACHORRO:
                switch (porte) {
                    case PEQUENO:
                        indiceBase = 0;
                        break;
                    case MEDIO:
                        indiceBase = 3;
                        break;
                    case GRANDE:
                        indiceBase = 6;
                        break;
                }
                break;
            case GATO:
                switch (porte) {
                    case PEQUENO:
                        indiceBase = 9;
                        break;
                    case MEDIO:
                        indiceBase = 11;
                        break;
                    case GRANDE:
                        indiceBase = 13;
                        break;
                }
                break;
        }

        switch (tipoServico) {
            case BANHO:
                return indiceBase;
            case TOSA:
                return indiceBase + 1;
            case PASSEIO:
                return indiceBase + 2;
        }

        throw new IllegalStateException();
    }

}
