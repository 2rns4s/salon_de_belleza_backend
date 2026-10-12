package com.sena.persistencia.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "calificaciones")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class CalificacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_calificacion")
    private Long idCalificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado", nullable = false)
    private EmpleadoEntity empleadoEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_servicio", nullable = false)
    private ServicioEntity servicioEntity;
}
