package com.ITCHA2026.GroovyfyMusic.repository;

import com.ITCHA2026.GroovyfyMusic.entities.Artista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Integer> {
<<<<<<< HEAD
    boolean existsByNombre(String nombre);
    boolean existsByNombreAndIdNot(String nombre, Integer id);
}
=======

    boolean existsByNombre(String nombre);
    boolean existsByNombreAndIdNot(String nombre,Integer id );
}
>>>>>>> 630898fdc79d248efd2f934c6e9053c9a0091c9e
