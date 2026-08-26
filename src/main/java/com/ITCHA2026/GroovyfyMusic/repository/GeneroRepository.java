package com.ITCHA2026.GroovyfyMusic.repository;

import com.ITCHA2026.GroovyfyMusic.entities.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Integer> {

    boolean existByNombre(String nombre);
    boolean existByNombreAndIdnot(String nombre, Integer id);
}
