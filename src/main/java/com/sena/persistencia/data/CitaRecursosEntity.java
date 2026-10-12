package com.sena.persistencia.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "citas_recursos")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class CitaRecursosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita_recurso")
    private Long idCitaRecurso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cita", nullable = false)
    private CitaEntity citaEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recurso", nullable = false)
    private RecursoEntity recursoEntity;

}
