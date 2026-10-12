package com.sena.persistencia.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recursos")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class RecursoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recurso")
    private Long idRecurso;

    @Column(unique = true)
    private String nombre;

    @Column(name = "cantidad_total")
    private int cantidadTotal;
}
