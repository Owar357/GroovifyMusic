package com.ITCHA2026.GroovyfyMusic.repository;

import com.ITCHA2026.GroovyfyMusic.entities.Role;
import com.ITCHA2026.GroovyfyMusic.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    boolean existsByNombre(Roles nombre);
    Optional<Role> findByNombre(Roles nombre); // útil para asignar el rol por defecto ("USER") al registrar

}
