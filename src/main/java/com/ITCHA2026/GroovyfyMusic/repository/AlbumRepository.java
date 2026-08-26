package com.ITCHA2026.GroovyfyMusic.repository;

import   com.ITCHA2026.GroovyfyMusic.entities.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {

    List<Album> findByArtistaId(Integer artistaId);

    List<Album> findByNombreContainingIgnoreCase(String nombre);

    boolean existsByNombreAndArtistaId(String nombre, Integer artistaId);

    boolean existsByNombreAndArtistaIdAndIdNot(String nombre, Integer artistaId, Integer id);
}
