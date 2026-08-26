package com.ITCHA2026.GroovyfyMusic.security;

import com.ITCHA2026.GroovyfyMusic.entities.Usuario;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
@Getter
public class UsuarioPrincipal implements UserDetails {

    private  final Integer id;
    private final String alias;
    private final String correo;
    private final String password;
    private final String rol;
    private final boolean activo;

    public UsuarioPrincipal(Usuario usuario) {
        this.id = usuario.getId();
        this.alias = usuario.getAlias();
        this.correo = usuario.getCorreo();
        this.password = usuario.getPassword();
        this.rol = usuario.getRol().getNombre().name();
        this.activo = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Spring security espera el prefijo "ROLE_"
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.rol));
    }

    @Override
    public String getUsername() {
        // Puedes usar alias o correo como username
        return this.correo;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return this.activo; }


}
