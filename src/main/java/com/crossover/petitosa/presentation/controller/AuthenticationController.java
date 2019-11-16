package com.crossover.petitosa.presentation.controller;

import com.crossover.petitosa.business.service.UsuarioService;
import com.crossover.petitosa.presentation.dto.LoginDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private ResponseEntity<String> logar(@RequestBody @Valid LoginDto loginDto) {
        if (usuarioService.authenticate(loginDto)) {
            return new ResponseEntity<String>("Sucesso", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("E-mail e/ou senha inválido(s)", HttpStatus.UNAUTHORIZED);
        }
    }

}
