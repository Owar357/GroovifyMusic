package com.ITCHA2026.GroovyfyMusic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "canciones", schema = "public")
public class Cancion implements Serializable {

    private  static  final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    @Column(nullable = false, length = 150)
    private  String nombre;

    @Column(name = "fecha_lanzamiento" , nullable = false )
    private LocalDate fechaLanzamiento;

    @Column(name = "duracion_segundos",nullable = false)
    private  Integer duracionSegundos;

    @Column(name = "portada_url",length = 300 , nullable = false)
    private String portada;

    @Column(name = "archivo_audio_url",nullable = false, length = 300)
    private String archivoAudio;

    //private  Album album;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id",  nullable = false)
    private  Usuario usuario;

}
