package com.ITCHA2026.GroovyfyMusic.repository;

import com.ITCHA2026.GroovyfyMusic.entities.Cancion;
import com.ITCHA2026.GroovyfyMusic.interfaces.ICancionPopular;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CancionRepository extends JpaRepository<Cancion, Integer> {

    List<Cancion> findByNombreContainingIgnoreCase(String nombre);

    List<Cancion> findByArtistaAliasContainingIgnoreCase(String alias);

    List<Cancion> findByNombreContainingIgnoreCaseAndArtistaAliasContainingIgnoreCase(String nombre, String alias);

    boolean existsByNombreAndArtistaId(String nombre, Integer artistaId);

    @Query("SELECT c.id as id, c.nombre as nombre, c.duracionSegundos as duracionSegundos, " +
            "c.portada as portada, c.archivoAudio as archivoAudio, COUNT(r.id) as reproducciones " +
            "FROM Cancion c " +
            "LEFT JOIN Reproduccion r ON r.cancion.id = c.id " +
            "WHERE c.artista.id = :artistaId " +
            "GROUP BY c.id, c.nombre, c.duracionSegundos, c.portada, c.archivoAudio " +
            "ORDER BY COUNT(r.id) DESC")
    List<ICancionPopular> findPopularesByArtistaId(@Param("artistaId") Integer artistaId, Pageable pageable);

    List<Cancion> findByAlbumId(Integer albumId);

    @Query("SELECT COALESCE(SUM(c.duracionSegundos), 0) FROM Cancion c WHERE c.album.id = :albumId")
    Integer sumDuracionSegundosByAlbumId(@Param("albumId") Integer albumId);
}