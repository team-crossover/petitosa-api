package com.crossover.petitosa.business.service;

import com.crossover.petitosa.business.entity.*;
import com.crossover.petitosa.business.enums.RoleUsuario;
import com.crossover.petitosa.data.repository.PrestadorRepository;
import com.crossover.petitosa.presentation.dto.NovoPrestadorDto;
import com.crossover.petitosa.presentation.dto.PrestadorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PrestadorService extends CrudService<Prestador, Long, PrestadorRepository> {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private ContaBancariaService contaService;

    public PrestadorDto add(NovoPrestadorDto novoPrestadorDto) {

        if (usuarioService.findByEmail(novoPrestadorDto.getEmail()) != null)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "E-mail já cadastrado");
        if (novoPrestadorDto.getSenha() == null || novoPrestadorDto.getSenha().isEmpty())
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Senha de novo prestador não pode ser vazia");

        Usuario usuario = Usuario.builder()
                .email(novoPrestadorDto.getEmail())
                .senha(novoPrestadorDto.getSenha())
                .role(RoleUsuario.PRESTADOR)
                .build();
        usuario = usuarioService.save(usuario);

        Endereco endereco = enderecoService.findEnderecoByCep(novoPrestadorDto.getEndereco().getCep());
        endereco.setLogradouro(novoPrestadorDto.getEndereco().getLogradouro());
        endereco = enderecoService.save(endereco);

        ContaBancaria conta = ContaBancaria.builder()
                .tipo(novoPrestadorDto.getContaBancaria().getTipo())
                .numero(novoPrestadorDto.getContaBancaria().getNumero())
                .digito(novoPrestadorDto.getContaBancaria().getDigito())
                .agencia(novoPrestadorDto.getContaBancaria().getAgencia())
                .build();
        conta = contaService.save(conta);

        Prestador prestador = Prestador.builder()
                .usuario(usuario)
                .nome(novoPrestadorDto.getNome())
                .genero(novoPrestadorDto.getGenero())
                .dataNascimento(novoPrestadorDto.getDataNascimento())
                .descricao(novoPrestadorDto.getDescricao())
                .servicosPrestados(Arrays.asList(novoPrestadorDto.getServicosPrestados()))
                .precos(Arrays.asList(novoPrestadorDto.getPrecos()))
                .endereco(endereco)
                .imgPerfil(novoPrestadorDto.getImgPerfil())
                .contaBancaria(conta)
                .build();
        prestador = save(prestador);

        return PrestadorDto.fromPrestador(prestador, this);
    }

    public PrestadorDto update(Long id, NovoPrestadorDto novosDadosDto) {
        Prestador prestador = findById(id);
        if (prestador == null)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "ID não encontrado");

        // Atualiza usuario (senha apenas se presente)
        Usuario usuario = prestador.getUsuario();
        usuario.setEmail(novosDadosDto.getEmail());
        if (novosDadosDto.getSenha() != null && !novosDadosDto.getSenha().isEmpty())
            usuario.setSenha(novosDadosDto.getSenha());
        usuario = usuarioService.save(usuario);

        // Atualiza endereco
        Endereco endereco = enderecoService.findEnderecoByCep(novosDadosDto.getEndereco().getCep());
        endereco.setLogradouro(novosDadosDto.getEndereco().getLogradouro());
        endereco.setId(prestador.getEndereco().getId());
        endereco = enderecoService.save(endereco);

        // Atualiza conta
        ContaBancaria conta = ContaBancaria.builder()
                .id(prestador.getContaBancaria().getId())
                .tipo(prestador.getContaBancaria().getTipo())
                .numero(prestador.getContaBancaria().getNumero())
                .digito(prestador.getContaBancaria().getDigito())
                .agencia(prestador.getContaBancaria().getAgencia())
                .build();
        conta = contaService.save(conta);

        // Atualiza demais dados (imagem apenas se presente)
        prestador.setUsuario(usuario);
        prestador.setNome(novosDadosDto.getNome());
        prestador.setGenero(novosDadosDto.getGenero());
        prestador.setDataNascimento(novosDadosDto.getDataNascimento());
        prestador.setDescricao(novosDadosDto.getDescricao());
        prestador.setServicosPrestados(new ArrayList(Arrays.asList(novosDadosDto.getServicosPrestados())));
        prestador.setPrecos(new ArrayList(Arrays.asList(novosDadosDto.getPrecos())));
        prestador.setEndereco(endereco);
        if (novosDadosDto.getImgPerfil() != null && !novosDadosDto.getImgPerfil().isEmpty())
            prestador.setImgPerfil(novosDadosDto.getImgPerfil());
        prestador.setContaBancaria(conta);
        prestador = save(prestador);

        return PrestadorDto.fromPrestador(prestador, this);
    }

    public double calculateNotaMedia(Prestador prestador) {
        double sum = 0.0;
        long count = 0;
        for (Servico servico : prestador.getServicos())
            if (servico.getAvaliacao() != null) {
                sum += servico.getAvaliacao().getNota();
                count++;
            }
        return count <= 0 ? 5.0 : sum / count;
    }

    public List<String> getComentarios(Prestador prestador) {
        return prestador.getServicos().stream()
                .filter(s -> s.getAvaliacao() != null)
                .map(s -> s.getAvaliacao().getTexto())
                .filter(t -> t != null && !t.isEmpty())
                .collect(Collectors.toList());
    }
}
