package com.ITCHA2026.GroovyfyMusic.interfaces;

import com.ITCHA2026.GroovyfyMusic.entities.Reproduccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IReproduccionRepository extends JpaRepository<Reproduccion, Integer> {

    List<Reproduccion> findByCancionArtistaIdAndReproducidoEnBetween(
            Integer artistaId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin
    );
}
