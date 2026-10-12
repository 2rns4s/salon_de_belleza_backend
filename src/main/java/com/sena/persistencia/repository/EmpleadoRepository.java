package com.sena.persistencia.repository;

import com.sena.persistencia.data.EmpleadoEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity, Long> {

    @EntityGraph(attributePaths = {"franjasLaborales", "calificaciones", "calificaciones.servicioEntity"})
    Optional<EmpleadoEntity> findById(Long idEmpleado);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = {"franjasLaborales"})
    @Query("SELECT e FROM EmpleadoEntity e WHERE e.idEmpleado = :idEmpleado")
    Optional<EmpleadoEntity> findByIdEmpleadoWithFranjasAndLock(@Param("idEmpleado") Long idEmpleado);

    Boolean existsByCorreo(String correo);

    boolean existsByIdEmpleado(Long idEmpleado);

    Optional<EmpleadoEntity> findByCorreo(String correo);
}
