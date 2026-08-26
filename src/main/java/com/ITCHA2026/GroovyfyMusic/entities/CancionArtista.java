package com.ITCHA2026.GroovyfyMusic.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "cancion_artista")
public class CancionArtista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artista_id",nullable = false)
    private Artista artista;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancion_id",nullable = false)
    private Cancion cancion;

    @CreationTimestamp
    @Column(name = "creado_en",nullable = false, updatable = false)
    private LocalDateTime creadoEn;
}
