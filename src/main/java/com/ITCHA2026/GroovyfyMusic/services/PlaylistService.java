package com.ITCHA2026.GroovyfyMusic.services;

import com.ITCHA2026.GroovyfyMusic.dto.CancionResponseDTO;
import com.ITCHA2026.GroovyfyMusic.dto.PlaylistRegistroDTO;
import com.ITCHA2026.GroovyfyMusic.dto.PlaylistResponseDTO;
import com.ITCHA2026.GroovyfyMusic.entities.Cancion;
import com.ITCHA2026.GroovyfyMusic.entities.Playlist;
import com.ITCHA2026.GroovyfyMusic.entities.PlaylistCancion;
import com.ITCHA2026.GroovyfyMusic.entities.Usuario;
import com.ITCHA2026.GroovyfyMusic.enums.TipoPlaylist;
import com.ITCHA2026.GroovyfyMusic.exceptions.ResourceNotFoundException;
import com.ITCHA2026.GroovyfyMusic.interfaces.IPlaylistService;
import com.ITCHA2026.GroovyfyMusic.mappers.CancionMapper;
import com.ITCHA2026.GroovyfyMusic.mappers.PlaylistMapper;
import com.ITCHA2026.GroovyfyMusic.repository.CancionRepository;
import com.ITCHA2026.GroovyfyMusic.repository.PlaylistCancionRepository;
import com.ITCHA2026.GroovyfyMusic.repository.PlaylistRepository;
import com.ITCHA2026.GroovyfyMusic.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistService implements IPlaylistService {

    private final PlaylistRepository playlistRepository;
    private final PlaylistCancionRepository playlistCancionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CancionRepository cancionRepository;
    private final PlaylistMapper playlistMapper;
    private final CancionMapper cancionMapper;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public PlaylistResponseDTO crearPlaylist(PlaylistRegistroDTO dto, Integer usuarioId, MultipartFile portada) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));

        Playlist playlist = playlistMapper.toEntity(dto);
        playlist.setTipo(TipoPlaylist.PERSONAL);
        playlist.setUsuario(usuario);

        if (portada != null && !portada.isEmpty()) {
            String portadaUrl =  cloudinaryService.uploadImage(portada, "playlists");
            playlist.setPortada(portadaUrl);
        }

        return playlistMapper.toDTO(playlistRepository.save(playlist));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaylistResponseDTO> obtenerPlaylistsUsuario(Integer usuarioId) {
        return playlistMapper.toDtoList(playlistRepository.findByUsuarioId(usuarioId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaylistResponseDTO> obtenerDestacadas() {
        return playlistMapper.toDtoList(playlistRepository.findByTipo(TipoPlaylist.DESTACADA));
    }

    @Override
    @Transactional(readOnly = true)
    public PlaylistResponseDTO obtenerPorId(Integer id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist no encontrada con id: " + id));
        return playlistMapper.toDTO(playlist);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CancionResponseDTO> obtenerCancionesDePlaylist(Integer playlistId) {
        if (!playlistRepository.existsById(playlistId)) {
            throw new ResourceNotFoundException("Playlist no encontrada con id: " + playlistId);
        }

        return playlistCancionRepository.findByPlaylistId(playlistId)
                .stream()
                .map(pc -> cancionMapper.toDTO(pc.getCancion()))
                .toList();
    }

    @Override
    @Transactional
    public void agregarCancion(Integer playlistId, Integer cancionId, Integer usuarioId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist no encontrada con id: " + playlistId));

        validarPermisoModificacion(playlist, usuarioId);

        Cancion cancion = cancionRepository.findById(cancionId)
                .orElseThrow(() -> new ResourceNotFoundException("Canción no encontrada con id: " + cancionId));

        if (playlistCancionRepository.existsByPlaylistIdAndCancionId(playlistId, cancionId)) {
            return;
        }

        PlaylistCancion pc = new PlaylistCancion();
        pc.setPlaylist(playlist);
        pc.setCancion(cancion);
        playlistCancionRepository.save(pc);
    }

    @Override
    @Transactional
    public void quitarCancion(Integer playlistId, Integer cancionId, Integer usuarioId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist no encontrada con id: " + playlistId));

        validarPermisoModificacion(playlist, usuarioId);

        playlistCancionRepository.findByPlaylistIdAndCancionId(playlistId, cancionId)
                .ifPresent(playlistCancionRepository::delete);
    }

    @Override
    @Transactional
    public void eliminarPlaylist(Integer id, Integer usuarioId) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist no encontrada con id: " + id));

        if (playlist.getTipo() != TipoPlaylist.PERSONAL) {
            throw new IllegalArgumentException("Esta playlist no se puede eliminar.");
        }
        if (!playlist.getUsuario().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("No puedes eliminar una playlist que no es tuya.");
        }

        playlistRepository.delete(playlist);
    }

    @Override
    @Transactional
    public boolean toggleMeGusta(Integer usuarioId, Integer cancionId) {
        Playlist favoritos = obtenerOCrearFavoritos(usuarioId);

        Cancion cancion = cancionRepository.findById(cancionId)
                .orElseThrow(() -> new ResourceNotFoundException("Canción no encontrada con id: " + cancionId));

        return playlistCancionRepository.findByPlaylistIdAndCancionId(favoritos.getId(), cancionId)
                .map(existente -> {
                    playlistCancionRepository.delete(existente);
                    return false;
                })
                .orElseGet(() -> {
                    PlaylistCancion pc = new PlaylistCancion();
                    pc.setPlaylist(favoritos);
                    pc.setCancion(cancion);
                    playlistCancionRepository.save(pc);
                    return true;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeMeGusta(Integer usuarioId, Integer cancionId) {
        return playlistRepository.findByUsuarioIdAndTipo(usuarioId, TipoPlaylist.FAVORITOS)
                .map(fav -> playlistCancionRepository.existsByPlaylistIdAndCancionId(fav.getId(), cancionId))
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CancionResponseDTO> obtenerCancionesMeGusta(Integer usuarioId) {
        return playlistRepository.findByUsuarioIdAndTipo(usuarioId, TipoPlaylist.FAVORITOS)
                .map(fav -> playlistCancionRepository.findByPlaylistId(fav.getId())
                        .stream()
                        .map(pc -> cancionMapper.toDTO(pc.getCancion()))
                        .toList())
                .orElse(List.of());
    }

    private Playlist obtenerOCrearFavoritos(Integer usuarioId) {
        return playlistRepository.findByUsuarioIdAndTipo(usuarioId, TipoPlaylist.FAVORITOS)
                .orElseGet(() -> {
                    Usuario usuario = usuarioRepository.findById(usuarioId)
                            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));
                    Playlist favoritos = new Playlist();
                    favoritos.setNombre("Tus me gusta");
                    favoritos.setTipo(TipoPlaylist.FAVORITOS);
                    favoritos.setUsuario(usuario);
                    return playlistRepository.save(favoritos);
                });
    }

    private void validarPermisoModificacion(Playlist playlist, Integer usuarioId) {
        if (playlist.getTipo() == TipoPlaylist.FAVORITOS) {
            throw new IllegalArgumentException("Usa el endpoint de me-gusta para esta playlist.");
        }

        if (playlist.getTipo() == TipoPlaylist.PERSONAL
                && !playlist.getUsuario().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("No puedes modificar una playlist que no es tuya.");
        }

        if (playlist.getTipo() == TipoPlaylist.DESTACADA) {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));

            if (!usuario.getRol().getNombre().name().equals("ARTISTA")) {
                throw new IllegalArgumentException("Solo los artistas pueden agregar canciones a playlists destacadas.");
            }
        }
    }
}