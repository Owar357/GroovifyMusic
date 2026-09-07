package com.ITCHA2026.GroovyfyMusic.entities;

import com.ITCHA2026.GroovyfyMusic.enums.TipoPlaylist;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "playlists", schema = "public")
public class Playlist implements Serializable {
    private  static  final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 150, nullable = false)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(length = 300)
    private String portada;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private TipoPlaylist tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;

    @CreationTimestamp
    @Column(name = "creada_en", nullable = false, updatable = false)
    private LocalDateTime creadaEn;
}
