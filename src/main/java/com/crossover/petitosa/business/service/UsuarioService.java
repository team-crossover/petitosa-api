package com.crossover.petitosa.business.service;

import com.crossover.petitosa.business.entity.Usuario;
import com.crossover.petitosa.data.repository.UsuarioRepository;
import com.crossover.petitosa.presentation.dto.LoginDto;
import com.crossover.petitosa.presentation.dto.UsuarioDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;

@Service
@Transactional
public class UsuarioService extends CrudService<Usuario, Long, UsuarioRepository> {

    public Usuario findByEmail(String email) {
        return getRepository().findByEmail(email).orElse(null);
    }

    public UsuarioDto authenticate(LoginDto loginDto) {
        Usuario usuario = findByEmail(loginDto.getEmail());
        if (usuario == null || !usuario.getSenha().equals(loginDto.getSenha())) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }

        return UsuarioDto.fromUsuario(usuario);
    }

}
