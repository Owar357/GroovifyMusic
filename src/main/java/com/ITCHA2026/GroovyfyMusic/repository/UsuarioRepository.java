package com.ITCHA2026.GroovyfyMusic.repository;

import com.ITCHA2026.GroovyfyMusic.entities.Usuario;
import com.ITCHA2026.GroovyfyMusic.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer>{

    boolean existsByCorreo(String correo);
    Optional<Usuario> findByAlias(String alias);
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreoAndIdNot(String correo, Integer id);
    List<Usuario> findByRolNombre(String nombreRol);
    List<Usuario> findByAliasContainingIgnoreCaseAndRolNombre(String alias, Roles nombreRol);
}

