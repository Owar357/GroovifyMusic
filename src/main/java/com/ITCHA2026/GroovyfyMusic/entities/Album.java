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
    private Integer id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false)
    private LocalDateTime fechaLanzamiento ;

    @Column(length = 300)
    private String portada;

    @Column(length = 6)
    private String duracion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artista_id")
    private Artista artista;





}
