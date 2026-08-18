package com.ITCHA2026.GroovyfyMusic.entities;

import com.ITCHA2026.GroovyfyMusic.enums.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "roles", schema = "public")
public class Role implements Serializable {
     private  static  final  long  serialVersionUID = 1L;

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private  Integer id;

      @Enumerated(EnumType.STRING)
      @Column(nullable = false , unique = true, length = 25)
      private Roles nombre;

      @CreationTimestamp
      @Column(name = "creado_en",nullable = false, updatable = false)
      private LocalDateTime creadoEn;
}
