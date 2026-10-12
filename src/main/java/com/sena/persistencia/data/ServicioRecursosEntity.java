package com.sena.persistencia.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "servicio_recursos")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ServicioRecursosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio_recursos")
    private Long idServicioRecursos;

    @Column(name = "cantidad_requerida", nullable = false)
    private int cantidadRequerida;

    @ManyToOne
    @JoinColumn(name = "id_recurso")
    private RecursoEntity recursoEntity;

    @ManyToOne
    @JoinColumn(name = "id_servicio")
    private ServicioEntity servicioEntity;
}
