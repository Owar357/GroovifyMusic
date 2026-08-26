package com.ITCHA2026.GroovyfyMusic.repository;

import   com.ITCHA2026.GroovyfyMusic.entities.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {




}
