package com.sena.persistencia.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "servicios")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ServicioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Long idServicio;

    @Column(unique = true, nullable = false)
    private String nombre;

    @Column(nullable = false)
    private int duracion;

    @OneToMany(mappedBy = "servicioEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ServicioRecursosEntity> servicioRecursos = new ArrayList<>();

    @OneToMany(mappedBy = "servicioEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    List<CalificacionEntity> calificaciones = new ArrayList<>();
}
