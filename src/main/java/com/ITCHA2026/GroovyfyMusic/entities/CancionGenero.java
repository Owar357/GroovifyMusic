package com.ITCHA2026.GroovyfyMusic.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cancion_genero" )
public class CancionGenero implements Serializable {
private static final long serialVersionUID = 1L ;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cancion_id", nullable = false)
  private Cancion cancion;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "genero_id", nullable = false)
  private Genero genero;

  @CreationTimestamp
  @Column(name = "creado_en",nullable = false, updatable = false)
  private LocalDateTime creadoEn;
}
