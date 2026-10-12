package com.sena.persistencia.repository;

import com.sena.persistencia.data.ServicioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServicioRepository extends JpaRepository<ServicioEntity, Long> {
    Optional<ServicioEntity> findByNombreIgnoreCase(String nombre);
}
