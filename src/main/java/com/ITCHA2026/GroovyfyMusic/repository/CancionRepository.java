package com.ITCHA2026.GroovyfyMusic.repository;

import com.ITCHA2026.GroovyfyMusic.entities.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CancionRepository extends JpaRepository<Cancion,Integer> {
    boolean existsByNombreAndUsuarioId(String nombre, Integer usuarioId);
    List<Cancion> findByUsuarioId(Integer usuarioId);
    List<Cancion> findByNombreContainingIgnoreCase(String nombre);

}
