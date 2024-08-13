package com.example.exampleproject.model.security.dto;

import com.example.exampleproject.model.security.entity.Usuario;

import java.util.List;
import lombok.Generated;

public class CadastroUsuarioDTO {
    private Usuario usuario;
    private List<String> permissoes;

    @Generated
    public Usuario getUsuario() {
        return this.usuario;
    }

    @Generated
    public List<String> getPermissoes() {
        return this.permissoes;
    }

    @Generated
    public void setUsuario(final Usuario usuario) {
        this.usuario = usuario;
    }

    @Generated
    public void setPermissoes(final List<String> permissoes) {
        this.permissoes = permissoes;
    }

    @Generated
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CadastroUsuarioDTO)) {
            return false;
        } else {
            CadastroUsuarioDTO other = (CadastroUsuarioDTO) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$usuario = this.getUsuario();
                Object other$usuario = other.getUsuario();
                if (this$usuario == null) {
                    if (other$usuario != null) {
                        return false;
                    }
                } else if (!this$usuario.equals(other$usuario)) {
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
        return other instanceof CadastroUsuarioDTO;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $usuario = this.getUsuario();
        result = result * 59 + ($usuario == null ? 43 : $usuario.hashCode());
        Object $permissoes = this.getPermissoes();
        result = result * 59 + ($permissoes == null ? 43 : $permissoes.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        String var10000 = String.valueOf(this.getUsuario());
        return "CadastroUsuarioDTO(usuario=" + var10000 + ", permissoes=" + String.valueOf(this.getPermissoes()) + ")";
    }
}
