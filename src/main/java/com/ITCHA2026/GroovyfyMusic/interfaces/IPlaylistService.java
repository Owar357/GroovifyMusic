package com.ITCHA2026.GroovyfyMusic.interfaces;

import com.ITCHA2026.GroovyfyMusic.dto.CancionResponseDTO;
import com.ITCHA2026.GroovyfyMusic.dto.PlaylistRegistroDTO;
import com.ITCHA2026.GroovyfyMusic.dto.PlaylistResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPlaylistService {
    PlaylistResponseDTO crearPlaylist(PlaylistRegistroDTO dto, Integer usuarioId, MultipartFile portada);
    List<PlaylistResponseDTO> obtenerPlaylistsUsuario(Integer usuarioId);
    List<PlaylistResponseDTO> obtenerDestacadas();
    PlaylistResponseDTO obtenerPorId(Integer id);
    List<CancionResponseDTO> obtenerCancionesDePlaylist(Integer playlistId);
    void agregarCancion(Integer playlistId, Integer cancionId, Integer usuarioId);
    void quitarCancion(Integer playlistId, Integer cancionId, Integer usuarioId);
    void eliminarPlaylist(Integer id, Integer usuarioId);
    boolean toggleMeGusta(Integer usuarioId, Integer cancionId);
    boolean existeMeGusta(Integer usuarioId, Integer cancionId);
    List<CancionResponseDTO> obtenerCancionesMeGusta(Integer usuarioId);
}