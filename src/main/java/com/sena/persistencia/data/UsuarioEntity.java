package com.sena.persistencia.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(unique = true, name = "nombre_usuario")
    private String nombreUsuario;

    @Column(nullable = false)
    private String contrasena;

    @Column(nullable = false)
    private String rol;

    @OneToOne
    @JoinColumn(name = "id_empleado")
    private EmpleadoEntity empleadoEntity;

    @Column(name = "mfa_enabled")
    private boolean mfaEnabled = false;

    private String mfaSecret;

    @Column(name = "codigos_recuperacion")
    private String codigosRecuperacion;

    @Column(nullable = false)
    private Boolean activo = true;
}
