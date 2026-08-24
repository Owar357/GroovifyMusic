package com.ITCHA2026.GroovyfyMusic.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
@Table(name = "album",schema = "public")
public class Album implements Serializable {
    private static final long  serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false, length = 150)
    private String Nombre;

    @Column(nullable = false)
    private LocalDateTime FechaLansamiento ;

    @Column(length = 300)
    private String Portada;

    @Column(length = 6)
    private String Duracion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artista_id")
    private Artista artista;





}
