package com.crossover.petitosa.business.service;

import com.crossover.petitosa.business.entity.Contratante;
import com.crossover.petitosa.business.entity.Endereco;
import com.crossover.petitosa.business.entity.Usuario;
import com.crossover.petitosa.business.enums.RoleUsuario;
import com.crossover.petitosa.data.repository.ContratanteRepository;
import com.crossover.petitosa.presentation.dto.ContratanteDto;
import com.crossover.petitosa.presentation.dto.NovoContratanteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class ContratanteService extends CrudService<Contratante, Long, ContratanteRepository> {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EnderecoService enderecoService;

    public ContratanteDto add(NovoContratanteDto novoContratanteDto) {

        if (usuarioService.findByEmail(novoContratanteDto.getEmail()) != null)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "E-mail já cadastrado");
        if (novoContratanteDto.getSenha() == null || novoContratanteDto.getSenha().isEmpty())
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Senha de novo contratante não pode ser vazia");

        Usuario usuario = Usuario.builder()
                .email(novoContratanteDto.getEmail())
                .senha(novoContratanteDto.getSenha())
                .role(RoleUsuario.CONTRATANTE)
                .build();
        usuario = usuarioService.save(usuario);

        Endereco endereco = enderecoService.findEnderecoByCep(novoContratanteDto.getEndereco().getCep());
        endereco.setLogradouro(novoContratanteDto.getEndereco().getLogradouro());
        endereco = enderecoService.save(endereco);

        Contratante contratante = Contratante.builder()
                .usuario(usuario)
                .nome(novoContratanteDto.getNome())
                .genero(novoContratanteDto.getGenero())
                .dataNascimento(novoContratanteDto.getDataNascimento())
                .endereco(endereco)
                .build();
        contratante = save(contratante);

        return ContratanteDto.fromContratante(contratante);
    }

    public ContratanteDto update(Long id, NovoContratanteDto novosDadosDto) {
        Contratante contratante = findById(id);
        if (contratante == null)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "ID não encontrado");

        // Atualiza usuario (senha apenas se presente)
        Usuario usuario = contratante.getUsuario();
        usuario.setEmail(novosDadosDto.getEmail());
        if (novosDadosDto.getSenha() != null && !novosDadosDto.getSenha().isEmpty())
            usuario.setSenha(novosDadosDto.getSenha());
        usuario = usuarioService.save(usuario);

        // Atualiza endereco
        Endereco endereco = enderecoService.findEnderecoByCep(novosDadosDto.getEndereco().getCep());
        endereco.setLogradouro(novosDadosDto.getEndereco().getLogradouro());
        endereco.setId(contratante.getEndereco().getId());
        endereco = enderecoService.save(endereco);

        // Atualiza demais dados
        contratante.setUsuario(usuario);
        contratante.setNome(novosDadosDto.getNome());
        contratante.setGenero(novosDadosDto.getGenero());
        contratante.setDataNascimento(novosDadosDto.getDataNascimento());
        contratante.setEndereco(endereco);
        contratante = save(contratante);

        return ContratanteDto.fromContratante(contratante);
    }
}
