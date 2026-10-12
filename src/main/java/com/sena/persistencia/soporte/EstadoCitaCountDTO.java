package com.sena.persistencia.soporte;

/**
 * NOTA (entrega SENA):
 * Este record pertenece conceptualmente a la CAPA DE APLICACION del proyecto
 * original (dto.reporte.EstadoCitaCountDTO).
 *
 * Se incluye aqui porque CitaRepository lo usa en una consulta de proyeccion
 * JPQL (SELECT new ...). Sin el, el repositorio no compila. NO contiene logica
 * de negocio: es solo el contenedor del resultado de una consulta de reporte.
 */
public record EstadoCitaCountDTO(
        EstadoCita estado,
        Long cantidad
) {
}
