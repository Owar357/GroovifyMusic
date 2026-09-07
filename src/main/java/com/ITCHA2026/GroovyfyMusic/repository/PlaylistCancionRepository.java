package com.ITCHA2026.GroovyfyMusic.repository;

import com.ITCHA2026.GroovyfyMusic.entities.PlaylistCancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistCancionRepository extends JpaRepository<PlaylistCancion, Integer> {

    List<PlaylistCancion> findByPlaylistId(Integer playlistId);

    Optional<PlaylistCancion> findByPlaylistIdAndCancionId(Integer playlistId, Integer cancionId);

    boolean existsByPlaylistIdAndCancionId(Integer playlistId, Integer cancionId);
}