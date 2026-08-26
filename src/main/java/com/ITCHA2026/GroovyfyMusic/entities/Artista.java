package com.ITCHA2026.GroovyfyMusic.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
public class Artista implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String biografia;

    @Column(length = 300)
    private String imagen;

    @CreationTimestamp
    @Column(name = "creado_en",nullable = false, updatable = false)
    private LocalDateTime creadoEn;
}
