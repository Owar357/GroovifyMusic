package com.ITCHA2026.GroovyfyMusic.repository;

import com.ITCHA2026.GroovyfyMusic.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer>{

    boolean existsByCorreo(String correo);
    Optional<Usuario> findByAlias(String alias);
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreoAndIdNot(String correo, Integer id);
}

