package com.ITCHA2026.GroovyfyMusic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "usuarios" ,  schema = "public")
public class Usuario implements Serializable {

      private  static  final long serialVersionUID = 1L;

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private  Integer id;

      @Column(length = 100, nullable = false )
      private  String alias;

      @Column(length = 150, nullable = false, unique = true )
      private  String correo;

      @Column(length = 100, nullable = false)
      private  String password;

      @Column(name = "imagen_url",length = 300, nullable = false )
      private  String imagen;

      @Column(name = "fecha_nacimiento", nullable = false )
      private LocalDate fechaNacimiento;

            @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "rol_id", nullable = false)
            private Role rol;

      @CreationTimestamp
      @Column(name = "creado_en",nullable = false, updatable = false)
      private LocalDateTime creadoEn;
}
