package com.ITCHA2026.GroovyfyMusic.services;

import com.ITCHA2026.GroovyfyMusic.dto.ReproduccionRegistroDTO;
import com.ITCHA2026.GroovyfyMusic.dto.ReproduccionResponseDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Cancion;
import com.ITCHA2026.GroovyfyMusic.entities.Reproduccion;
import com.ITCHA2026.GroovyfyMusic.entities.Usuario;
import com.ITCHA2026.GroovyfyMusic.exceptions.ResourceNotFoundException;
import com.ITCHA2026.GroovyfyMusic.interfaces.IReproduccionService;
import com.ITCHA2026.GroovyfyMusic.repository.CancionRepository;
import com.ITCHA2026.GroovyfyMusic.repository.ReproduccionRepository;
import com.ITCHA2026.GroovyfyMusic.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReproduccionService implements IReproduccionService {

    private final ReproduccionRepository reproduccionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CancionRepository cancionRepository;

    @Override
    @Transactional
    public ReproduccionResponseDTO registrarReproduccion(ReproduccionRegistroDTO dto, Integer usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));


        Cancion cancion = cancionRepository.findById(dto.getCancionId())
                .orElseThrow(() -> new ResourceNotFoundException("Canción no encontrada con id: " + dto.getCancionId()));


        Reproduccion reproduccion = new Reproduccion();
        reproduccion.setUsuario(usuario);
        reproduccion.setCancion(cancion);


        Reproduccion guardada = reproduccionRepository.save(reproduccion);


        return mapToResponseDTO(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReproduccionResponseDTO> obtenerHistorialPorUsuario(Integer usuarioId) {

        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId);
        }


        return reproduccionRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReproduccionResponseDTO> obtenerReproduccionesPorCancion(Integer cancionId) {

        if (!cancionRepository.existsById(cancionId)) {
            throw new ResourceNotFoundException("Canción no encontrada con id: " + cancionId);
        }


        return reproduccionRepository.findByCancionId(cancionId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long contarReproduccionesPorCancion(Integer cancionId) {

        if (!cancionRepository.existsById(cancionId)) {
            throw new ResourceNotFoundException("Canción no encontrada con id: " + cancionId);
        }


        return reproduccionRepository.countByCancionId(cancionId);
    }


    private ReproduccionResponseDTO mapToResponseDTO(Reproduccion reproduccion) {
        ReproduccionResponseDTO dto = new ReproduccionResponseDTO();
        dto.setId(reproduccion.getId());
        dto.setUsuarioId(reproduccion.getUsuario().getId());
        dto.setUsuarioAlias(reproduccion.getUsuario().getAlias());
        dto.setCancionId(reproduccion.getCancion().getId());
        dto.setCancionNombre(reproduccion.getCancion().getNombre());
        dto.setReproducidoEn(reproduccion.getReproducidoEn());
        return dto;
    }
}