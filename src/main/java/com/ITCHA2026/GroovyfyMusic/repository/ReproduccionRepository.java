package com.ITCHA2026.GroovyfyMusic.repository;

import com.ITCHA2026.GroovyfyMusic.entities.Reproduccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReproduccionRepository extends JpaRepository<Reproduccion,Integer> {
    List<Reproduccion> findByUsuarioIdOrderByReproducidoEnDesc(Integer usuarioId);
    long countByCancionId(Integer cancionId);
}
