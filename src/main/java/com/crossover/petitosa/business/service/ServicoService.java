package com.crossover.petitosa.business.service;

import com.crossover.petitosa.business.entity.*;
import com.crossover.petitosa.business.enums.*;
import com.crossover.petitosa.data.repository.AvaliacaoRepository;
import com.crossover.petitosa.data.repository.ServicoRepository;
import com.crossover.petitosa.data.repository.ServicosPorAnimalRepository;
import com.crossover.petitosa.presentation.dto.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Log4j2
@Service
@Transactional
public class ServicoService extends CrudService<Servico, Long, ServicoRepository> {

    public static final BigDecimal TAXA_PETITOSA = new BigDecimal("0.25");

    public static final BigDecimal TAXA_DESISTENCIA = new BigDecimal("8.00");

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

    @Autowired
    private ServicosPorAnimalRepository servicosPorAnimalRepository;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private MoneyService moneyService;

    public List<Servico> listarServicosPorContratante(Long idContratante) {
        return getRepository().findAllByContratanteId(idContratante);
    }

    public List<Servico> listarServicosPorPrestador(Long idPrestador) {
        return getRepository().findAllByPrestadorId(idPrestador);
    }

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

            BigDecimal precoServico = calcularPrecoTodosServicos(p, filtroServicoDto.getServicosPorAnimais()).setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal precoTaxaPetitosa = calcularTaxaPetitosa(precoServico).setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal precoTaxaDesistencia = contratante.getUsuario().getTaxaDesistenciaAPagar().setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal precoTotal = precoServico.add(precoTaxaPetitosa).add(precoTaxaDesistencia).setScale(2, RoundingMode.HALF_EVEN);

            // Evita prestadores cujo preço total é acima do máximo
            if (filtroServicoDto.getPrecoTotalMaximo() != null && precoTotal.compareTo(filtroServicoDto.getPrecoTotalMaximo()) > 0)
                continue;

            double distancia = calcularDistanciaPrestador(contratante, p);

            // Evita prestadores cuja distância é acima da máxima
            if (filtroServicoDto.getDistanciaMaxima() != null && distancia > filtroServicoDto.getDistanciaMaxima())
                continue;

            // Cria o DTO de prestador encontrado
            prestadorEncontradoDtos.add(PrestadorEncontradoDto.builder()
                    .nome(p.getNome())
                    .descricao(p.getDescricao())
                    .avaliacao(prestadorService.calculateNotaMedia(p))
                    .distancia(distancia)
                    .idPrestador(p.getId())
                    .imgPerfil(p.getImgPerfil())
                    .precoServico(precoServico)
                    .precoTaxaPetitosa(precoTaxaPetitosa)
                    .precoTaxaDesistencia(precoTaxaDesistencia)
                    .precoTotal(precoTotal)
                    .build());
        }
        return prestadorEncontradoDtos;
    }

    public ServicoDto solicitar(SolicitacaoServicoDto solicitacaoServicoDto) {
        Contratante contratante = solicitacaoServicoDto.getIdContratante() == null ? null : contratanteService.findById(solicitacaoServicoDto.getIdContratante());
        if (contratante == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID de contratante não encontrado");

        Prestador prestador = solicitacaoServicoDto.getIdPrestador() == null ? null : prestadorService.findById(solicitacaoServicoDto.getIdPrestador());
        if (prestador == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID de prestador não encontrado");

        if (!solicitacaoServicoDto.getDataEsperada().isAfter(LocalDateTime.now()))
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Data do serviço não pode ser antes de agora");

        if (!prestadorPrestaTodosServicos(prestador, solicitacaoServicoDto.getServicosPorAnimais()))
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Prestador não presta algum dos serviços selecionados");

        List<ServicosPorAnimal> servicosPorAnimal = new ArrayList<>();
        for (ServicosPorAnimalDto spaDto : solicitacaoServicoDto.getServicosPorAnimais()) {
            Animal animal = animalService.findById(spaDto.getIdAnimal());
            if (animal == null)
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID de animal não encontrado");

            servicosPorAnimal.add(ServicosPorAnimal.builder()
                    .animal(animal)
                    .tiposServico(Arrays.asList(spaDto.getTiposServicos()))
                    .build());
        }

        BigDecimal precoServico = calcularPrecoTodosServicos(prestador, solicitacaoServicoDto.getServicosPorAnimais()).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal precoTaxaPetitosa = calcularTaxaPetitosa(precoServico).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal precoTaxaDesistencia = contratante.getUsuario().getTaxaDesistenciaAPagar().setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal precoTotal = precoServico.add(precoTaxaPetitosa).add(precoTaxaDesistencia).setScale(2, RoundingMode.HALF_EVEN);

        Servico servico = Servico.builder()
                .contratante(contratante)
                .prestador(prestador)
                .enderecoServico(contratante.getEndereco())
                .observacoes(solicitacaoServicoDto.getObservacoes())
                .precoServico(precoServico)
                .precoTaxaPetitosa(precoTaxaPetitosa)
                .precoTaxaDesistencia(precoTaxaDesistencia)
                .precoTotal(precoTotal)
                .dataSolicitacao(LocalDateTime.now())
                .dataEsperadaRealizacao(solicitacaoServicoDto.getDataEsperada())
                .status(StatusServico.PENDENTE)
                .build();
        servico = save(servico);

        for (ServicosPorAnimal spa : servicosPorAnimal)
            spa.setServico(servico);
        servicosPorAnimalRepository.saveAll(servicosPorAnimal);
        servico.setServicosPorAnimais(servicosPorAnimal);

        return ServicoDto.fromServico(servico);
    }

    public ServicoDto aceitar(long idServico) {
        Servico servico = findById(idServico);
        if (servico == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID de servico não encontrado");

        if (servico.getStatus() != StatusServico.PENDENTE)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Só pode aceitar serviços pendentes");

        servico.setStatus(StatusServico.ACEITO);
        servico.setDataAceitacao(LocalDateTime.now());
        save(servico);

        return ServicoDto.fromServico(servico);
    }

    public ServicoDto rejeitar(long idServico) {
        Servico servico = findById(idServico);
        if (servico == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID de servico não encontrado");

        if (servico.getStatus() != StatusServico.PENDENTE)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Só pode rejeitar serviços pendentes");

        servico.setStatus(StatusServico.REJEITADO);
        servico.setDataRejeicao(LocalDateTime.now());
        save(servico);

        return ServicoDto.fromServico(servico);
    }

    public ServicoDto desistir(long idUsuarioDesistente, long idServico) {
        Usuario usuario = usuarioService.findById(idUsuarioDesistente);
        if (usuario == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID de usuário não encontrado");

        Servico servico = findById(idServico);
        if (servico == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID de servico não encontrado");

        if (servico.getContratante() != usuario.getContratante() && servico.getPrestador() != usuario.getPrestador())
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Só pode ser desistido pelo contratante ou pelo prestador");

        if (servico.getStatus() != StatusServico.PENDENTE && servico.getStatus() != StatusServico.ACEITO)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Só pode desistir de serviços pendentes ou aceitos");

        // Adiciona a taxa de desistência caso o serviço já tenha sido aceito anteriormente
        if (servico.getStatus() == StatusServico.ACEITO) {
            usuario.setTaxaDesistenciaAPagar(usuario.getTaxaDesistenciaAPagar().add(TAXA_DESISTENCIA).setScale(2, RoundingMode.HALF_EVEN));
            usuario = usuarioService.save(usuario);
        }

        servico.setUsuarioDesistente(usuario);
        servico.setStatus(StatusServico.DESISTIDO);
        servico.setDataDesistencia(LocalDateTime.now());
        save(servico);

        return ServicoDto.fromServico(servico);
    }

    public ServicoDto iniciarRealizacao(long idServico) {
        Servico servico = findById(idServico);
        if (servico == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID de servico não encontrado");

        if (servico.getStatus() != StatusServico.ACEITO)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Só pode iniciar realização de serviços aceitos");

        servico.setStatus(StatusServico.EM_ANDAMENTO);
        servico.setDataInicioRealizacao(LocalDateTime.now());
        save(servico);

        return ServicoDto.fromServico(servico);
    }

    public ServicoDto terminarRealizacao(long idServico) {
        Servico servico = findById(idServico);
        if (servico == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID de servico não encontrado");

        if (servico.getStatus() != StatusServico.EM_ANDAMENTO)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Só pode terminar realização de serviços em andamento");

        moneyService.processarPagamentoServico(servico);

        servico.setStatus(StatusServico.TERMINADO);
        servico.setDataTerminoRealizacao(LocalDateTime.now());
        save(servico);

        return ServicoDto.fromServico(servico);
    }

    public ServicoDto avaliar(long idServico, NovaAvaliacaoDto avaliacaoDto) {
        Servico servico = findById(idServico);
        if (servico == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID de servico não encontrado");

        if (servico.getStatus() != StatusServico.TERMINADO)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Só pode avaliar serviços terminados");

        if (servico.getAvaliacao() != null)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Serviço já foi avaliado");

        Avaliacao avaliacao = Avaliacao.builder()
                .dataAvaliacao(LocalDateTime.now())
                .nota(avaliacaoDto.getNota())
                .texto(avaliacaoDto.getTexto())
                .servico(servico)
                .build();
        avaliacaoRepository.save(avaliacao);
        servico.setAvaliacao(avaliacao);

        return ServicoDto.fromServico(servico);
    }

    private BigDecimal calcularTaxaPetitosa(BigDecimal valorParaPrestador) {
        return valorParaPrestador.multiply(TAXA_PETITOSA);
    }

    private BigDecimal calcularPrecoTodosServicos(Prestador prestador, ServicosPorAnimalDto[] servicos) {
        BigDecimal sum = BigDecimal.ZERO;
        for (ServicosPorAnimalDto servicosPorAnimalDto : servicos)
            sum = sum.add(calcularPrecoServicosPorAnimal(prestador, servicosPorAnimalDto));
        return sum;
    }

    private BigDecimal calcularPrecoServicosPorAnimal(Prestador prestador, ServicosPorAnimalDto servicosPorAnimalDto) {
        Animal animal = animalService.findById(servicosPorAnimalDto.getIdAnimal());
        if (animal == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "ID de animal não encontrado");

        BigDecimal sum = BigDecimal.ZERO;
        List<BigDecimal> precos = prestador.getPrecos();
        for (TipoServico tipoServico : servicosPorAnimalDto.getTiposServicos()) {
            int indice = getIndiceArrayDeServicos(animal.getEspecie(), animal.getPorte(), tipoServico);
            sum = sum.add(precos.get(indice));
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