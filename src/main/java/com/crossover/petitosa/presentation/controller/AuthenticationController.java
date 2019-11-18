package com.crossover.petitosa.presentation.controller;

import com.crossover.petitosa.business.service.UsuarioService;
import com.crossover.petitosa.presentation.dto.LoginDto;
import com.crossover.petitosa.presentation.dto.UsuarioDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthenticationController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/api/v1/authentication/login")
    @ApiOperation("Autenticar usuário")
    private UsuarioDto logar(@RequestBody @Valid LoginDto loginDto) {
        return usuarioService.authenticate(loginDto);
    }

}
