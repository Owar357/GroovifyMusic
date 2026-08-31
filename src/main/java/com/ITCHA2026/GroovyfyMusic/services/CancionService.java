package com.ITCHA2026.GroovyfyMusic.services;

import com.ITCHA2026.GroovyfyMusic.dto.CancionFiltroDTO;
import com.ITCHA2026.GroovyfyMusic.dto.CancionRegistroDTO;
import com.ITCHA2026.GroovyfyMusic.dto.CancionResponseDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Album;
import com.ITCHA2026.GroovyfyMusic.entities.Cancion;
import com.ITCHA2026.GroovyfyMusic.entities.Genero;
import com.ITCHA2026.GroovyfyMusic.entities.Usuario;
import com.ITCHA2026.GroovyfyMusic.exceptions.ConflictException;
import com.ITCHA2026.GroovyfyMusic.exceptions.ResourceNotFoundException;
import com.ITCHA2026.GroovyfyMusic.interfaces.ICancionService;
import com.ITCHA2026.GroovyfyMusic.mappers.CancionMapper;
import com.ITCHA2026.GroovyfyMusic.repository.AlbumRepository;
import com.ITCHA2026.GroovyfyMusic.repository.CancionRepository;
import com.ITCHA2026.GroovyfyMusic.repository.GeneroRepository;
import com.ITCHA2026.GroovyfyMusic.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CancionService implements ICancionService {

    private final CancionRepository cancionRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlbumRepository albumRepository;
    private final GeneroRepository generoRepository;
    private final CancionMapper cancionMapper;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public CancionResponseDTO register(CancionRegistroDTO dto, MultipartFile portada, MultipartFile archivoAudio, Integer usuarioId) {
        Usuario artista = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // ✅ CORREGIDO: existsByNombreAndArtistaId
        if (cancionRepository.existsByNombreAndArtistaId(dto.getNombre(), usuarioId)) {
            throw new ConflictException("Ya existe una canción con ese nombre para este artista");
        }

        Cancion cancion = cancionMapper.toEntity(dto);
        cancion.setArtista(artista);

        if (portada != null && !portada.isEmpty()) {
            String portadaUrl = cloudinaryService.uploadImage(portada, "canciones/portadas");
            cancion.setPortada(portadaUrl);
        }
        if (archivoAudio != null && !archivoAudio.isEmpty()) {
            String audioUrl = cloudinaryService.uploadAudio(archivoAudio, "canciones/audios");
            cancion.setArchivoAudio(audioUrl);
        }

        if (dto.getAlbumId() != null) {
            Album album = albumRepository.findById(dto.getAlbumId())
                    .orElseThrow(() -> new ResourceNotFoundException("Álbum no encontrado con id: " + dto.getAlbumId()));
            cancion.setAlbum(album);
        } else {
            cancion.setAlbum(null);
        }

        if (dto.getGenerosIds() != null && !dto.getGenerosIds().isEmpty()) {
            List<Genero> generos = generoRepository.findAllById(dto.getGenerosIds());
            if (generos.size() != dto.getGenerosIds().size()) {
                throw new ResourceNotFoundException("Uno o más géneros no existen");
            }
            cancion.setGeneros(generos);
        }

        Cancion cancionGuardada = cancionRepository.save(cancion);
        return cancionMapper.toDTO(cancionGuardada);
    }

    @Override
    @Transactional
    public CancionResponseDTO update(Integer id, CancionRegistroDTO dto, MultipartFile portada, MultipartFile archivoAudio) {
        Cancion cancionExistente = cancionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Canción no encontrada con id: " + id));

        if (dto.getNombre() != null) cancionExistente.setNombre(dto.getNombre());
        if (dto.getFechaLanzamiento() != null) cancionExistente.setFechaLanzamiento(dto.getFechaLanzamiento());
        if (dto.getDuracionSegundos() != null) cancionExistente.setDuracionSegundos(dto.getDuracionSegundos());

        if (portada != null && !portada.isEmpty()) {
            String portadaUrl = cloudinaryService.uploadImage(portada, "canciones/portadas");
            cancionExistente.setPortada(portadaUrl);
        }
        if (archivoAudio != null && !archivoAudio.isEmpty()) {
            String audioUrl = cloudinaryService.uploadAudio(archivoAudio, "canciones/audios");
            cancionExistente.setArchivoAudio(audioUrl);
        }

        if (dto.getAlbumId() != null) {
            Album album = albumRepository.findById(dto.getAlbumId())
                    .orElseThrow(() -> new ResourceNotFoundException("Álbum no encontrado con id: " + dto.getAlbumId()));
            cancionExistente.setAlbum(album);
        } else {
            cancionExistente.setAlbum(null);
        }

        if (dto.getGenerosIds() != null) {
            if (dto.getGenerosIds().isEmpty()) {
                cancionExistente.setGeneros(new ArrayList<>());
            } else {
                List<Genero> generos = generoRepository.findAllById(dto.getGenerosIds());
                if (generos.size() != dto.getGenerosIds().size()) {
                    throw new ResourceNotFoundException("Uno o más géneros no existen");
                }
                cancionExistente.setGeneros(generos);
            }
        }

        Cancion cancionActualizada = cancionRepository.save(cancionExistente);
        return cancionMapper.toDTO(cancionActualizada);
    }

    @Override
    @Transactional(readOnly = true)
    public CancionResponseDTO findById(Integer id) {
        Cancion cancion = cancionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Canción no encontrada con id: " + id));
        return cancionMapper.toDTO(cancion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CancionResponseDTO> findAll() {
        List<Cancion> canciones = cancionRepository.findAll();
        return cancionMapper.toDtoList(canciones);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CancionResponseDTO> search(CancionFiltroDTO filtro) {
        String nombre = filtro.getNombre();
        String artista = filtro.getArtista();

        if (nombre != null && !nombre.isEmpty() && artista != null && !artista.isEmpty()) {
            return cancionMapper.toDtoList(
                    cancionRepository.findByNombreContainingIgnoreCaseAndArtistaAliasContainingIgnoreCase(nombre, artista)
            );
        } else if (nombre != null && !nombre.isEmpty()) {
            return cancionMapper.toDtoList(
                    cancionRepository.findByNombreContainingIgnoreCase(nombre)
            );
        } else if (artista != null && !artista.isEmpty()) {
            return cancionMapper.toDtoList(
                    cancionRepository.findByArtistaAliasContainingIgnoreCase(artista)
            );
        } else {
            return findAll();
        }
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!cancionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Canción no encontrada con id: " + id);
        }
        cancionRepository.deleteById(id);
    }
}