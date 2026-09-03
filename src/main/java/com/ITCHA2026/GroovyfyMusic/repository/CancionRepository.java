package com.ITCHA2026.GroovyfyMusic.repository;

import com.ITCHA2026.GroovyfyMusic.entities.Cancion;
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

    List<Cancion> findByArtistaId(Integer artistaId);

    @Query("SELECT COALESCE(SUM(c.duracionSegundos), 0) FROM Cancion c WHERE c.album.id = :albumId")
    Integer sumDuracionSegundosByAlbumId(@Param("albumId") Integer albumId);
}