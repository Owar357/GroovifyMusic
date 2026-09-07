package com.ITCHA2026.GroovyfyMusic.repository;

import com.ITCHA2026.GroovyfyMusic.entities.Playlist;
import com.ITCHA2026.GroovyfyMusic.enums.TipoPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist,Integer> {
    List<Playlist> findByUsuarioId(Integer usuarioId);
    Optional<Playlist> findByUsuarioIdAndTipo(Integer usuarioId, TipoPlaylist tipo);
    List<Playlist> findByTipo(TipoPlaylist tipo);
}
