package com.example.exampleproject.model.security.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.exampleproject.model.security.entity.Grupo;
import com.example.exampleproject.model.security.entity.Usuario;
import com.example.exampleproject.model.security.entity.UsuarioGrupo;
import com.example.exampleproject.model.security.repository.GrupoRepository;
import com.example.exampleproject.model.security.repository.UsuarioGrupoRepository;
import com.example.exampleproject.model.security.repository.UsuarioRepository;

import lombok.Generated;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private final GrupoRepository grupoRepository;
    private final UsuarioGrupoRepository usuarioGrupoRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario salvar(Usuario usuario, List<String> grupos) {
        String senhaCriptografada = this.passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        this.repository.save(usuario);

        List<UsuarioGrupo> listaUsuarioGrupo = grupos.stream()
                .map(nomeGrupo -> {
                    Optional<Grupo> possivelGrupo = this.grupoRepository.findByNome(nomeGrupo);
                    return possivelGrupo
                            .map(grupo -> new UsuarioGrupo(usuario, grupo))
                            .orElse(null);
                })
                .filter(grupo -> grupo != null)
                .collect(Collectors.toList());

        this.usuarioGrupoRepository.saveAll(listaUsuarioGrupo);
        return obterUsuarioComPermissoes(usuario.getLogin());
    }

    public Usuario obterUsuarioComPermissoes(String login) {
        Optional<Usuario> usuarioOptional = this.repository.findByLogin(login);
        if (usuarioOptional.isEmpty()) {
            return null;
        } else {
            Usuario usuario = (Usuario) usuarioOptional.get();
            List<String> permissoes = this.usuarioGrupoRepository.findPermissoesByUsuario(usuario);
            usuario.setPermissoes(permissoes);
            return usuario;
        }
    }

    @Generated
    public UsuarioService(final UsuarioRepository repository, final GrupoRepository grupoRepository,
            final UsuarioGrupoRepository usuarioGrupoRepository, final PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.grupoRepository = grupoRepository;
        this.usuarioGrupoRepository = usuarioGrupoRepository;
        this.passwordEncoder = passwordEncoder;
    }
}