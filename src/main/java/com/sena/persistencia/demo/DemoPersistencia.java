package com.sena.persistencia.demo;

import com.sena.persistencia.data.*;
import com.sena.persistencia.repository.*;
import com.sena.persistencia.soporte.EstadoCita;
import com.sena.persistencia.soporte.EstadoCitaCountDTO;
import com.sena.persistencia.soporte.ServicioTopDTO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Demostracion de la capa de persistencia.
 *
 * Un CommandLineRunner es un bean que Spring ejecuta UNA VEZ, despues de que la
 * aplicacion termina de arrancar (contexto listo, conexion abierta y tablas
 * creadas por ddl-auto=update).
 *
 * En el proyecto propuesto, los repositorios se usan a traves de PUERTOS del
 * dominio (los PersistenceAdapter). Aqui, al ser una muestra de la capa
 * de persistencia, este runner cumple ese mismo rol: inyecta los repositorios
 * directamente y ejecuta un guion CRUD + consultas personalizadas, imprimiendo
 * cada paso por consola para evidenciar la interaccion real con PostgreSQL.
 *
 * Se marca @Transactional para poder navegar las asociaciones LAZY (p. ej. las
 * franjas o calificaciones de un empleado) dentro de un unico contexto de
 * persistencia.
 */
@Component
public class DemoPersistencia implements CommandLineRunner {

    private final EmpleadoRepository empleadoRepository;
    private final ClienteRepository clienteRepository;
    private final ServicioRepository servicioRepository;
    private final RecursoRepository recursoRepository;
    private final CitaRepository citaRepository;
    private final UsuarioRepository usuarioRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public DemoPersistencia(EmpleadoRepository empleadoRepository,
                            ClienteRepository clienteRepository,
                            ServicioRepository servicioRepository,
                            RecursoRepository recursoRepository,
                            CitaRepository citaRepository,
                            UsuarioRepository usuarioRepository) {
        this.empleadoRepository = empleadoRepository;
        this.clienteRepository = clienteRepository;
        this.servicioRepository = servicioRepository;
        this.recursoRepository = recursoRepository;
        this.citaRepository = citaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        titulo("INICIO DE LA DEMOSTRACION DE PERSISTENCIA JPA");

        limpiarDatosPrevios();

        // ---------------------------------------------------------------
        // 1. CREATE - Guardar entidades (repository.save)
        // ---------------------------------------------------------------
        titulo("1. CREATE - Insertando datos de prueba");

        EmpleadoEntity empleado = new EmpleadoEntity();
        empleado.setNombre("Laura");
        empleado.setApellido("Gomez");
        empleado.setCorreo("laura.gomez@salon.com");
        empleado.setTelefono("3001112233");
        empleado = empleadoRepository.save(empleado);
        System.out.println("  Empleado guardado -> id=" + empleado.getIdEmpleado());

        ClienteEntity cliente = new ClienteEntity();
        cliente.setNombre("Carlos");
        cliente.setApellido("Perez");
        // documento y telefono se cifran automaticamente via CryptoConverter al persistir.
        cliente.setDocumento("1098765432");
        cliente.setTelefono("3109998877");
        cliente = clienteRepository.save(cliente);
        System.out.println("  Cliente guardado  -> id=" + cliente.getIdCliente()
                + " (documento/telefono se guardan CIFRADOS en la DB)");

        ServicioEntity servicio = new ServicioEntity();
        servicio.setNombre("Corte de cabello");
        servicio.setDuracion(45);
        servicio = servicioRepository.save(servicio);
        System.out.println("  Servicio guardado -> id=" + servicio.getIdServicio());

        RecursoEntity recurso = new RecursoEntity();
        recurso.setNombre("Silla de corte");
        recurso.setCantidadTotal(3);
        recurso = recursoRepository.save(recurso);
        System.out.println("  Recurso guardado  -> id=" + recurso.getIdRecurso());

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNombreUsuario("laura.gomez");
        usuario.setContrasena("hash-de-ejemplo");
        usuario.setRol("EMPLEADO");
        usuario.setEmpleadoEntity(empleado);
        usuario = usuarioRepository.save(usuario);
        System.out.println("  Usuario guardado  -> id=" + usuario.getIdUsuario());

        // Cita CONFIRMADA de hoy, con un recurso reservado.
        LocalDateTime inicio = LocalDateTime.now().withHour(10).withMinute(0).withSecond(0).withNano(0);
        CitaEntity cita = new CitaEntity();
        cita.setEmpleado(empleado);
        cita.setCliente(cliente);
        cita.setServicio(servicio);
        cita.setFechaInicio(inicio);
        cita.setFechaFin(inicio.plusMinutes(servicio.getDuracion()));
        cita.setEstado(EstadoCita.CONFIRMADA);

        CitaRecursosEntity reserva = new CitaRecursosEntity();
        reserva.setCitaEntity(cita);
        reserva.setRecursoEntity(recurso);
        cita.getRecursosReservados().add(reserva);

        cita = citaRepository.save(cita);
        System.out.println("  Cita guardada     -> id=" + cita.getIdCita()
                + " estado=" + cita.getEstado() + " (con 1 recurso reservado)");

        // ---------------------------------------------------------------
        // 2. READ - Lecturas basicas (findAll / findById) del CRUD de JpaRepository
        // ---------------------------------------------------------------
        titulo("2. READ - Lecturas basicas (CRUD heredado de JpaRepository)");
        System.out.println("  Total empleados en DB: " + empleadoRepository.count());
        System.out.println("  Total servicios en DB: " + servicioRepository.count());
        System.out.println("  findById(empleado): "
                + empleadoRepository.findById(empleado.getIdEmpleado())
                    .map(e -> e.getNombre() + " " + e.getApellido())
                    .orElse("NO ENCONTRADO"));

        // ---------------------------------------------------------------
        // 3. QUERIES PERSONALIZADAS (los metodos propios de cada repositorio)
        // ---------------------------------------------------------------
        titulo("3. QUERIES PERSONALIZADAS");

        // EmpleadoRepository.findByCorreo / existsByCorreo (derivadas del nombre)
        System.out.println("  existsByCorreo('laura.gomez@salon.com'): "
                + empleadoRepository.existsByCorreo("laura.gomez@salon.com"));
        System.out.println("  findByCorreo(...): "
                + empleadoRepository.findByCorreo("laura.gomez@salon.com")
                    .map(EmpleadoEntity::getNombre).orElse("NO ENCONTRADO"));

        // ServicioRepository.findByNombreIgnoreCase
        System.out.println("  servicio.findByNombreIgnoreCase('corte de cabello'): "
                + servicioRepository.findByNombreIgnoreCase("corte de cabello").isPresent());

        // RecursoRepository.findFirstByNombreContainsIgnoreCase
        System.out.println("  recurso.findFirstByNombreContainsIgnoreCase('silla'): "
                + recursoRepository.findFirstByNombreContainsIgnoreCase("silla")
                    .map(RecursoEntity::getNombre).orElse("NO ENCONTRADO"));

        // UsuarioRepository.findByNombreUsuario + exists por relacion anidada
        System.out.println("  usuario.findByNombreUsuario('laura.gomez'): "
                + usuarioRepository.findByNombreUsuario("laura.gomez").isPresent());
        System.out.println("  usuario.existsUsuarioByEmpleadoEntity_IdEmpleado(" + empleado.getIdEmpleado() + "): "
                + usuarioRepository.existsUsuarioByEmpleadoEntity_IdEmpleado(empleado.getIdEmpleado()));

        // ClienteRepository.findByNombreContainingIgnoreCase (campo NO cifrado)
        System.out.println("  cliente.findByNombreContainingIgnoreCase('car'): "
                + clienteRepository.findByNombreContainingIgnoreCase("car").size() + " resultado(s)");

        // CitaRepository: queries @Query con JPQL y parametros temporales
        LocalDateTime desde = inicio.minusHours(1);
        LocalDateTime hasta = inicio.plusHours(1);
        System.out.println("  cita.existsCitaSolapadaParaEmpleado(...): "
                + citaRepository.existsCitaSolapadaParaEmpleado(empleado.getIdEmpleado(), desde, hasta));
        System.out.println("  cita.existsCitaSolapadaParaRecurso(...): "
                + citaRepository.existsCitaSolapadaParaRecurso(recurso.getIdRecurso(), desde, hasta));
        System.out.println("  cita.contarCitasSolapadasPorRecurso(...): "
                + citaRepository.contarCitasSolapadasPorRecurso(recurso.getIdRecurso(), desde, hasta));

        LocalDateTime inicioDia = inicio.toLocalDate().atStartOfDay();
        List<CitaEntity> citasHoy = citaRepository.findCitasDelDia(inicioDia, inicioDia.plusDays(1));
        System.out.println("  cita.findCitasDelDia(hoy): " + citasHoy.size() + " cita(s)");

        // Proyecciones con SELECT new (DTOs de reporte)
        List<EstadoCitaCountDTO> conteos = citaRepository.contarCitasPorEstadoEnRango(
                inicioDia, inicioDia.plusDays(1));
        System.out.println("  cita.contarCitasPorEstadoEnRango(hoy):");
        conteos.forEach(c -> System.out.println("      - " + c.estado() + " = " + c.cantidad()));

        List<ServicioTopDTO> top = citaRepository.encontrarTopServiciosRealizados(
                inicioDia.minusYears(1), inicioDia.plusDays(1), PageRequest.of(0, 5));
        System.out.println("  cita.encontrarTopServiciosRealizados(ultimo anio): "
                + top.size() + " servicio(s) con estado REALIZADA");

        // ---------------------------------------------------------------
        // 4. UPDATE - Modificar y volver a guardar (dirty checking)
        // ---------------------------------------------------------------
        titulo("4. UPDATE - Cambiando el estado de la cita a REALIZADA");
        cita.setEstado(EstadoCita.REALIZADA);
        citaRepository.save(cita);
        System.out.println("  Nuevo estado persistido: "
                + citaRepository.findById(cita.getIdCita())
                    .map(c -> c.getEstado().name()).orElse("?"));

        // ---------------------------------------------------------------
        // 5. DELETE - Eliminar la cita (deleteById del CRUD)
        // ---------------------------------------------------------------
        titulo("5. DELETE - Eliminando la cita de prueba");
        citaRepository.deleteById(cita.getIdCita());
        System.out.println("  La cita existe tras borrarla? "
                + citaRepository.existsById(cita.getIdCita()));

        // La demo deja la base vacia para que cada ejecucion parta del mismo estado.
        limpiarDatosPrevios();

        titulo("FIN DE LA DEMOSTRACION - La capa de persistencia funciona correctamente");
    }

    /**
     * Deja la base en un estado limpio para que la demo sea idempotente
     * (se puede ejecutar varias veces sin chocar con restricciones unique).
     */
    private void limpiarDatosPrevios() {
        citaRepository.deleteAll();
        usuarioRepository.deleteAll();
        clienteRepository.deleteAll();
        servicioRepository.deleteAll();
        recursoRepository.deleteAll();
        empleadoRepository.deleteAll();
        entityManager.flush();
    }

    private void titulo(String texto) {
        System.out.println();
        System.out.println("================================================================");
        System.out.println("  " + texto);
        System.out.println("================================================================");
    }
}
