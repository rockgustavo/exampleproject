package com.example.exampleproject.model.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.exampleproject.model.security.dto.CadastroUsuarioDTO;
import com.example.exampleproject.model.security.entity.Usuario;
import com.example.exampleproject.model.security.service.UsuarioService;

import lombok.Generated;

@RestController
@RequestMapping({ "/usuarios" })
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Usuario> salvar(@RequestBody CadastroUsuarioDTO body) {
        Usuario usuarioSalvo = this.usuarioService.salvar(body.getUsuario(), body.getPermissoes());
        return ResponseEntity.ok(usuarioSalvo);
    }

    @Generated
    public UsuarioController(final UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
}
