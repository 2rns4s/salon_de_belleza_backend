package com.sena.persistencia.data;

import com.sena.persistencia.converter.CryptoConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    private String nombre;

    private String apellido;

    @Convert(converter = CryptoConverter.class)
    @Column(unique = true, nullable = false, length = 255)
    private String documento;

    @Convert(converter = CryptoConverter.class)
    @Column(unique = true, nullable = false, length = 255)
    private String telefono;
}
