package com.sena.persistencia.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "franjas_laborales")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FranjaLaboralEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_franja_laboral")
    private Long idFranjaLaboral;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dia;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    private EmpleadoEntity empleadoEntity;
}
