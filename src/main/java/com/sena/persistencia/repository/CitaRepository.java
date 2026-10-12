package com.sena.persistencia.repository;

import com.sena.persistencia.soporte.EstadoCitaCountDTO;
import com.sena.persistencia.soporte.ServicioTopDTO;
import com.sena.persistencia.data.CitaEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<CitaEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("""
        SELECT COUNT(c) > 0
        FROM CitaEntity c
        WHERE c.empleado.idEmpleado = :idEmpleado
        AND c.estado <> 'CANCELADA'
        AND (c.fechaInicio < :fechaFin AND c.fechaFin > :fechaInicio)
    """)
    boolean existsCitaSolapadaParaEmpleado(
            @Param("idEmpleado") Long idEmpleado,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    @Query("""
        SELECT COUNT(c) > 0 
        FROM CitaEntity c 
        JOIN c.recursosReservados cr
        WHERE cr.recursoEntity.idRecurso = :idRecurso
        AND c.estado <> 'CANCELADA'
        AND (c.fechaInicio < :fechaFin AND c.fechaFin > :fechaInicio)
    """)
    boolean existsCitaSolapadaParaRecurso(
            @Param("idRecurso") Long idRecurso,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("""
        SELECT COUNT(c)
        FROM CitaEntity c
        JOIN c.recursosReservados cr
        WHERE cr.recursoEntity.idRecurso = :idRecurso
        AND c.estado <> 'CANCELADA'
        AND (c.fechaInicio < :fechaFin AND c.fechaFin > :fechaInicio)
    """)
    int contarCitasSolapadasPorRecurso(
            @Param("idRecurso") Long idRecurso,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    @Query("SELECT c FROM CitaEntity c WHERE c.fechaInicio >= :start AND c.fechaInicio < :end AND c.estado <> 'CANCELADA'")
    List<CitaEntity> findCitasDelDia(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("""
        SELECT new com.sena.persistencia.soporte.EstadoCitaCountDTO(c.estado, COUNT(c.idCita))
        FROM CitaEntity c
        WHERE c.fechaInicio >= :inicio AND c.fechaInicio <= :fin
        GROUP BY c.estado
    """)
    List<EstadoCitaCountDTO> contarCitasPorEstadoEnRango(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    @Query("""
        SELECT new com.sena.persistencia.soporte.ServicioTopDTO(s.nombre, COUNT(c.idCita))
        FROM CitaEntity c
        JOIN c.servicio s
        WHERE c.fechaInicio >= :inicio AND c.fechaInicio <= :fin
          AND c.estado = 'REALIZADA'
        GROUP BY s.nombre
        ORDER BY COUNT(c.idCita) DESC
    """)
    List<ServicioTopDTO> encontrarTopServiciosRealizados(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            Pageable pageable); // Usamos Pageable para hacer el LIMIT de forma segura
}
