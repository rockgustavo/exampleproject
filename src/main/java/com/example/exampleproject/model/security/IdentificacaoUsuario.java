package com.example.exampleproject.model.security;

import java.util.ArrayList;
import java.util.List;

import lombok.Generated;

public class IdentificacaoUsuario {
    private String id;
    private String nome;
    private String login;
    private List<String> permissoes;

    public IdentificacaoUsuario(String id, String nome, String login, List<String> permissoes) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.permissoes = permissoes;
    }

    public List<String> getPermissoes() {
        if (this.permissoes == null) {
            this.permissoes = new ArrayList<>();
        }

        return this.permissoes;
    }

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getNome() {
        return this.nome;
    }

    @Generated
    public String getLogin() {
        return this.login;
    }

    @Generated
    public void setId(final String id) {
        this.id = id;
    }

    @Generated
    public void setNome(final String nome) {
        this.nome = nome;
    }

    @Generated
    public void setLogin(final String login) {
        this.login = login;
    }

    @Generated
    public void setPermissoes(final List<String> permissoes) {
        this.permissoes = permissoes;
    }

    @Generated
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof IdentificacaoUsuario)) {
            return false;
        } else {
            IdentificacaoUsuario other = (IdentificacaoUsuario) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label59: {
                    Object this$id = this.getId();
                    Object other$id = other.getId();
                    if (this$id == null) {
                        if (other$id == null) {
                            break label59;
                        }
                    } else if (this$id.equals(other$id)) {
                        break label59;
                    }

                    return false;
                }

                Object this$nome = this.getNome();
                Object other$nome = other.getNome();
                if (this$nome == null) {
                    if (other$nome != null) {
                        return false;
                    }
                } else if (!this$nome.equals(other$nome)) {
                    return false;
                }

                Object this$login = this.getLogin();
                Object other$login = other.getLogin();
                if (this$login == null) {
                    if (other$login != null) {
                        return false;
                    }
                } else if (!this$login.equals(other$login)) {
                    return false;
                }

                Object this$permissoes = this.getPermissoes();
                Object other$permissoes = other.getPermissoes();
                if (this$permissoes == null) {
                    if (other$permissoes != null) {
                        return false;
                    }
                } else if (!this$permissoes.equals(other$permissoes)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof IdentificacaoUsuario;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        Object $nome = this.getNome();
        result = result * 59 + ($nome == null ? 43 : $nome.hashCode());
        Object $login = this.getLogin();
        result = result * 59 + ($login == null ? 43 : $login.hashCode());
        Object $permissoes = this.getPermissoes();
        result = result * 59 + ($permissoes == null ? 43 : $permissoes.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = this.getId();
        return "IdentificacaoUsuario(id=" + var10000 + ", nome=" + this.getNome() + ", login=" + this.getLogin()
                + ", permissoes=" + String.valueOf(this.getPermissoes()) + ")";
    }

    @Generated
    public IdentificacaoUsuario() {
    }
}
