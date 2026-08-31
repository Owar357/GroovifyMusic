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
        System.out.println("🔍 BUSCANDO USUARIO POR CORREO: " + username);

        Usuario usuario = usuarioRepository.findByCorreo(username)
                .orElseThrow(() -> {
                    System.out.println("❌ USUARIO NO ENCONTRADO");
                    return new UsernameNotFoundException("Usuario o contraseña incorrectos");
                });

        System.out.println("✅ USUARIO ENCONTRADO: " + usuario.getCorreo());
        System.out.println("🔑 CONTRASEÑA EN BD (HASH): " + usuario.getPassword());
        System.out.println("🎭 ROL: " + usuario.getRol().getNombre());

        // Verificar que tenga un rol asignado
        if (usuario.getRol() == null) {
            System.out.println("❌ USUARIO SIN ROL");
            throw new UsernameNotFoundException("Usuario o contraseña incorrectos");
        }

        return new UsuarioPrincipal(usuario);
    }
}