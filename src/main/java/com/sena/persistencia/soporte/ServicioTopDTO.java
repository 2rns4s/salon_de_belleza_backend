package com.sena.persistencia.soporte;

/**
 * NOTA (entrega SENA):
 * Este record pertenece conceptualmente a la CAPA DE APLICACION del proyecto
 * original (dto.reporte.ServicioTopDTO).
 *
 * Se incluye aqui porque CitaRepository lo usa en una consulta de proyeccion
 * JPQL (SELECT new ...). Sin el, el repositorio no compila. NO contiene logica
 * de negocio: es solo el contenedor del resultado de una consulta de reporte.
 *
 * (Se eliminaron las anotaciones @Schema de OpenAPI/Swagger del original porque
 *  pertenecen a la capa web, ajena a la persistencia.)
 */
public record ServicioTopDTO(
        String nombreServicio,
        long cantidadRealizada
) {
}
