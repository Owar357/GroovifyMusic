package com.ITCHA2026.GroovyfyMusic.repository;


import com.ITCHA2026.GroovyfyMusic.entities.Artista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Integer> {

    boolean existByNombre(String nombre);
    boolean existByNombreAndIdNot(String nombre,Integer id );
}
