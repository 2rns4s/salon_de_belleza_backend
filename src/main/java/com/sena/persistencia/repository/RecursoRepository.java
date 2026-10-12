package com.sena.persistencia.repository;

import com.sena.persistencia.data.RecursoEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecursoRepository extends JpaRepository<RecursoEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM RecursoEntity r WHERE r.idRecurso = :idRecurso")
    Optional<RecursoEntity> findByIdRecursoWithLock(@Param("idRecurso") Long idRecurso);

    List<RecursoEntity> findByNombreIgnoreCase(String nombre);

    Optional<RecursoEntity> findFirstByNombreContainsIgnoreCase(String nombre);
}
