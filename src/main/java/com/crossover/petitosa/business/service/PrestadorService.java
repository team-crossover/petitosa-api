package com.crossover.petitosa.business.service;

import com.crossover.petitosa.business.entity.Endereco;
import com.crossover.petitosa.business.entity.Prestador;
import com.crossover.petitosa.business.entity.Usuario;
import com.crossover.petitosa.data.repository.PrestadorRepository;
import com.crossover.petitosa.presentation.dto.NovoPrestadorDto;
import com.crossover.petitosa.presentation.dto.PrestadorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class PrestadorService extends CrudService<Prestador, Long, PrestadorRepository> {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EnderecoService enderecoService;

    public PrestadorDto cadastrar(NovoPrestadorDto novoPrestadorDto) {

        if (usuarioService.findByEmail(novoPrestadorDto.getEmail()) != null)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "E-mail já cadastrado");
        if (novoPrestadorDto.getSenha() == null || novoPrestadorDto.getSenha().isEmpty())
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Senha de novo prestador não pode ser vazia");

        Usuario usuario = Usuario.builder()
                .email(novoPrestadorDto.getEmail())
                .senha(novoPrestadorDto.getSenha())
                .build();
        usuario = usuarioService.save(usuario);

        Endereco endereco = enderecoService.findEnderecoByCep(novoPrestadorDto.getEndereco().getCep());
        endereco.setLogradouro(novoPrestadorDto.getEndereco().getLogradouro());
        endereco = enderecoService.save(endereco);

        Prestador prestador = Prestador.builder()
                .usuario(usuario)
                .nome(novoPrestadorDto.getNome())
                .genero(novoPrestadorDto.getGenero())
                .dataNascimento(novoPrestadorDto.getDataNascimento())
                .descricao(novoPrestadorDto.getDescricao())
                .servicosPrestados(Arrays.asList(novoPrestadorDto.getServicosPrestados()))
                .precos(Arrays.asList(novoPrestadorDto.getPrecos()))
                .endereco(endereco)
                .build();
        prestador = save(prestador);

        return PrestadorDto.fromPrestador(prestador);
    }

    public PrestadorDto editar(Long id, NovoPrestadorDto novosDadosDto) {
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

        // Atualiza demais dados
        prestador.setUsuario(usuario);
        prestador.setNome(novosDadosDto.getNome());
        prestador.setGenero(novosDadosDto.getGenero());
        prestador.setDataNascimento(novosDadosDto.getDataNascimento());
        prestador.setDescricao(novosDadosDto.getDescricao());
        prestador.setServicosPrestados(new ArrayList(Arrays.asList(novosDadosDto.getServicosPrestados())));
        prestador.setPrecos(new ArrayList(Arrays.asList(novosDadosDto.getPrecos())));
        prestador.setEndereco(endereco);
        prestador = save(prestador);

        return PrestadorDto.fromPrestador(prestador);
    }
}
