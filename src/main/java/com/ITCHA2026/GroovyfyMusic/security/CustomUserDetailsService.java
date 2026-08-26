package com.ITCHA2026.GroovyfyMusic.security;


import com.ITCHA2026.GroovyfyMusic.entities.Usuario;
import com.ITCHA2026.GroovyfyMusic.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("Correo o contraseña incorrectos"));

        // Verificar que tenga un rol asignado
        if (usuario.getRol() == null) {
            throw new UsernameNotFoundException("Usuario o contraseña incorrectos");
        }

        return new UsuarioPrincipal(usuario);
    }

}
