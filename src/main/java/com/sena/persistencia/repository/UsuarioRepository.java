package com.sena.persistencia.repository;

import com.sena.persistencia.data.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    boolean existsUsuarioByEmpleadoEntity_IdEmpleado(Long idEmpleado);

    boolean existsUsuarioByNombreUsuario(String nombreUsuario);

    Optional<UsuarioEntity> findByNombreUsuario(String nombreUsuario);
}
