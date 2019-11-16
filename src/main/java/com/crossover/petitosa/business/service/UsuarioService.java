package com.crossover.petitosa.business.service;

import com.crossover.petitosa.business.entity.Usuario;
import com.crossover.petitosa.data.repository.UsuarioRepository;
import com.crossover.petitosa.presentation.dto.LoginDto;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService extends CrudService<Usuario, Long, UsuarioRepository> {

    public Usuario findByEmail(String email) {
        return getRepository().findByEmail(email).orElse(null);
    }

    public boolean authenticate(LoginDto loginDto) {
        Usuario usuario = findByEmail(loginDto.getEmail());
        return usuario != null && usuario.getSenha().equals(loginDto.getSenha());
    }

}
