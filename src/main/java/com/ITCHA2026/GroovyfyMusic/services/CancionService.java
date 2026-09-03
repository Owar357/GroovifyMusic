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
import com.mpatric.mp3agic.Mp3File; // 🟢 Importado mp3agic
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File; // 🟢 Importado File
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

            // 🟢 Calcula la duración directamente del archivo MP3
            Integer duracion = calcularDuracionSegundos(archivoAudio);
            if (duracion > 0) {
                cancion.setDuracionSegundos(duracion);
            }
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

        if (cancionGuardada.getAlbum() != null) {
            recalcularDuracionAlbum(cancionGuardada.getAlbum().getId());
        }

        return cancionMapper.toDTO(cancionGuardada);
    }

    @Override
    @Transactional
    public CancionResponseDTO update(Integer id, CancionRegistroDTO dto, MultipartFile portada, MultipartFile archivoAudio) {
        Cancion cancionExistente = cancionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Canción no encontrada con id: " + id));

        // Registrar ID del álbum anterior por si cambia de álbum
        Integer oldAlbumId = (cancionExistente.getAlbum() != null) ? cancionExistente.getAlbum().getId() : null;

        if (dto.getNombre() != null) cancionExistente.setNombre(dto.getNombre());
        if (dto.getFechaLanzamiento() != null) cancionExistente.setFechaLanzamiento(dto.getFechaLanzamiento());

        // Si mandan duracionSegundos por JSON se mantiene, pero si suben MP3 nuevo se sobreescribe abajo
        if (dto.getDuracionSegundos() != null) cancionExistente.setDuracionSegundos(dto.getDuracionSegundos());

        if (portada != null && !portada.isEmpty()) {
            String portadaUrl = cloudinaryService.uploadImage(portada, "canciones/portadas");
            cancionExistente.setPortada(portadaUrl);
        }

        if (archivoAudio != null && !archivoAudio.isEmpty()) {
            String audioUrl = cloudinaryService.uploadAudio(archivoAudio, "canciones/audios");
            cancionExistente.setArchivoAudio(audioUrl);

            Integer duracion = calcularDuracionSegundos(archivoAudio);
            if (duracion > 0) {
                cancionExistente.setDuracionSegundos(duracion);
            }
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

        if (cancionActualizada.getAlbum() != null) {
            recalcularDuracionAlbum(cancionActualizada.getAlbum().getId());
        }

        if (oldAlbumId != null && (cancionActualizada.getAlbum() == null || !oldAlbumId.equals(cancionActualizada.getAlbum().getId()))) {
            recalcularDuracionAlbum(oldAlbumId);
        }

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
        Cancion cancion = cancionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Canción no encontrada con id: " + id));

        Integer albumId = (cancion.getAlbum() != null) ? cancion.getAlbum().getId() : null;

        cancionRepository.delete(cancion);

        if (albumId != null) {
            recalcularDuracionAlbum(albumId);
        }
    }

    private void recalcularDuracionAlbum(Integer albumId) {
        if (albumId == null) return;

        Integer totalSegundos = cancionRepository.sumDuracionSegundosByAlbumId(albumId);
        if (totalSegundos == null) totalSegundos = 0;

        int minutos = totalSegundos / 60;
        int segundos = totalSegundos % 60;
        String duracionFormateada = String.format("%02d:%02d", minutos, segundos);

        albumRepository.findById(albumId).ifPresent(album -> {
            album.setDuracion(duracionFormateada);
            albumRepository.save(album);
        });
    }

    private Integer calcularDuracionSegundos(MultipartFile archivoAudio) {
        if (archivoAudio == null || archivoAudio.isEmpty()) {
            return 0;
        }

        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp_audio_", ".mp3");
            archivoAudio.transferTo(tempFile);

            Mp3File mp3file = new Mp3File(tempFile);
            return (int) mp3file.getLengthInSeconds();

        } catch (Exception e) {
            // Si hay un error leyendo el MP3, retorna 0 de forma segura
            return 0;
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
}