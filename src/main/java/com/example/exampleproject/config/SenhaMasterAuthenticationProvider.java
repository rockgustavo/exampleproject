package com.example.exampleproject.config;

import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.example.exampleproject.model.security.CustomAuthentication;
import com.example.exampleproject.model.security.IdentificacaoUsuario;

@Component
public class SenhaMasterAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        var login = authentication.getName();
        var senha = (String) authentication.getCredentials();

        String loginMaster = "master";
        String senhaMaster = "123";

        if (loginMaster.equals(login) && senhaMaster.equals(senha)) {

            IdentificacaoUsuario identificacaoUsuario = new IdentificacaoUsuario(
                    "ID_MASTER",
                    "Master",
                    loginMaster,
                    List.of("ADMIN"));

            return new CustomAuthentication(identificacaoUsuario);
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
