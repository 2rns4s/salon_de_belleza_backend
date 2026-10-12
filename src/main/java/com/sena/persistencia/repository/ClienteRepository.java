package com.sena.persistencia.repository;

import com.sena.persistencia.data.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

    Optional<ClienteEntity> findByDocumento(String documento);

    Optional<ClienteEntity> findByTelefono(String telefono);

    List<ClienteEntity> findByNombreContainingIgnoreCase(String nombre);

}
