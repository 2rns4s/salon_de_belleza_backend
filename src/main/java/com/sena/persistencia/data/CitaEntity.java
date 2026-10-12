package com.sena.persistencia.data;

import com.sena.persistencia.soporte.EstadoCita;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "citas")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class CitaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado", nullable = false)
    EmpleadoEntity empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    ClienteEntity cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_servicio", nullable = false)
    ServicioEntity servicio;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoCita estado;

    @OneToMany(mappedBy = "citaEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    List<CitaRecursosEntity> recursosReservados = new ArrayList<>();

}
