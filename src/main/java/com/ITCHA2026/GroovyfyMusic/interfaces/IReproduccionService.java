package com.ITCHA2026.GroovyfyMusic.interfaces;

import com.ITCHA2026.GroovyfyMusic.dto.ReproduccionFiltroDTO;
import com.ITCHA2026.GroovyfyMusic.dto.ReproduccionRegistroDTO;
import com.ITCHA2026.GroovyfyMusic.dto.ReproduccionResponseDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Reproduccion;

import java.util.List;

public interface IReproduccionService {
    ReproduccionResponseDTO registrarReproduccion(ReproduccionRegistroDTO dto, Integer usuarioId);
    List<ReproduccionResponseDTO> obtenerHistorialPorUsuario(Integer usuarioId);
    List<ReproduccionResponseDTO> obtenerReproduccionesPorCancion(Integer cancionId);
    long contarReproduccionesPorCancion(Integer cancionId);
}