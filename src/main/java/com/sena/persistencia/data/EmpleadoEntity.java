package com.sena.persistencia.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "empleados")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class EmpleadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Long idEmpleado;

    private String nombre;

    private String apellido;

    private String correo;

    @Column(unique = true, nullable = false)
    private String telefono;

    @OneToMany(mappedBy = "empleadoEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FranjaLaboralEntity> franjasLaborales = new ArrayList<>();

    @OneToMany(mappedBy = "empleadoEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CalificacionEntity> calificaciones = new ArrayList<>();

}
