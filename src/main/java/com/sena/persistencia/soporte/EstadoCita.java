package com.sena.persistencia.soporte;

/**
 * NOTA (entrega SENA):
 * Este enum pertenece conceptualmente a la CAPA DE DOMINIO del proyecto original
 * (com.salondebelleza...agendamiento.domain.entities.EstadoCita).
 *
 * Se incluye aqui porque CitaEntity lo usa como columna mapeada con
 * @Enumerated(EnumType.STRING). Sin el, la capa de persistencia no compila.
 * NO representa logica de negocio: es solo el conjunto de estados posibles
 * necesario para el mapeo JPA de la columna "estado".
 */
public enum EstadoCita {
    CONFIRMADA,
    CANCELADA,
    REALIZADA
}
