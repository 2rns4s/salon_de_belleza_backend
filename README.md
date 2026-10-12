# Entrega SENA — Capa de Persistencia JPA

Mini-proyecto Spring Boot como muestra de cómo se mapea el
modelo a la base de datos y cómo se interactúa con ella mediante repositorios de
Spring Data JPA.

---

## ¿Qué incluye?

| Paquete | Contenido | Rol |
|---|---|---|
| `com.sena.persistencia.data` | 10 **entities** JPA (`@Entity`) | Mapeo objeto-tabla |
| `com.sena.persistencia.repository` | 6 **repositories** (`JpaRepository`) | CRUD + consultas personalizadas |
| `com.sena.persistencia.converter` | `CryptoConverter` | Cifrado AES de columnas sensibles |
| `com.sena.persistencia.soporte` | `EstadoCita`, `EstadoCitaCountDTO`, `ServicioTopDTO` | Soporte mínimo (ver nota) |
| `com.sena.persistencia.demo` | `DemoPersistencia` | Demostración CRUD por consola |

### Entities

`EmpleadoEntity`, `ClienteEntity`, `ServicioEntity`, `RecursoEntity`,
`CitaEntity`, `UsuarioEntity`, `FranjaLaboralEntity`, `CalificacionEntity`,
`CitaRecursosEntity`, `ServicioRecursosEntity`.

Incluyen relaciones `@OneToMany` / `@ManyToOne` / `@OneToOne`, enums mapeados con
`@Enumerated(EnumType.STRING)` y columnas cifradas con `@Convert`.

### Repositories

Cada uno extiende `JpaRepository<Entity, Long>` (de ahí heredan el CRUD básico:
`save`, `findById`, `findAll`, `deleteById`, `count`, etc.) y añade **métodos
personalizados** para la lógica de negocio:

- **Derivados del nombre**: `findByCorreo`, `findByNombreIgnoreCase`,
  `existsByCorreo`, `findByNombreContainingIgnoreCase`, etc.
- **`@Query` con JPQL**: detección de citas solapadas por empleado/recurso,
  citas del día, conteos por estado y top de servicios realizados.
- **Bloqueos pesimistas** (`@Lock`) y **grafos de entidad** (`@EntityGraph`).

---

## Nota sobre el "soporte mínimo"

Tres clases NO pertenecen conceptualmente a persistencia pero son **necesarias
para que la capa compile de forma aislada** (están comentadas en el código):

- **`EstadoCita`** → es del **dominio**. La usa `CitaEntity` para mapear la
  columna `estado`.
- **`EstadoCitaCountDTO`** y **`ServicioTopDTO`** → son de la **aplicación**.
  Los usa `CitaRepository` en proyecciones JPQL (`SELECT new ...`).

El **`CryptoConverter`** sí es de persistencia: la *decisión* de que `documento`
y `telefono` sean sensibles es de negocio (registrada en los specs), pero el
*mecanismo* de cifrarlos a nivel de columna es un detalle de infraestructura
(un `AttributeConverter` de JPA).

---

## Requisitos

- **JDK 21**
- **Docker** (para la base de datos dedicada).

### Base de datos dedicada y aislada

Este mini-proyecto trae su **Contenedor PostgreSQL** (`docker-compose.yml`):
> Importante: la demo ejecuta `deleteAll()` al iniciar para ser idempotente.

Levantar el contenedor (desde esta carpeta):

```bash
docker compose up -d
```

Credenciales (ya configuradas en `application.properties`):

| Clave | Valor |
|---|---|
| URL | `jdbc:postgresql://localhost:5433/sena_persistencia_db` |
| Usuario | `user` |
| Contraseña | `password` |

Para apagarlo (y opcionalmente borrar sus datos con `-v`):

```bash
docker compose down       # detiene el contenedor
docker compose down -v    # ademas elimina el volumen (datos de la demo)
```

---

## Cómo ejecutar

Con el contenedor arriba, desde esta carpeta (`sena/persistencia-jpa`):

```bash
# Windows
.\gradlew.bat bootRun

# Linux / macOS
./gradlew bootRun
```

Al arrancar:

1. Spring Boot abre la conexión al contenedor PostgreSQL dedicado.
2. Hibernate **crea/actualiza las tablas automáticamente** (`ddl-auto=update`)
   a partir de las anotaciones de las entities.
3. El `CommandLineRunner` `DemoPersistencia` ejecuta un guión completo e imprime
   cada paso por consola.
   
---

## Qué demuestra la ejecución

`DemoPersistencia` resuelve el "¿cómo interactúo con la capa si está aislada?":
en el proyecto propuesto los repositorios se usan a través de **puertos** del dominio
(los `PersistenceAdapter`). Aquí ese papel lo cumple el `CommandLineRunner`, que
inyecta los repositorios directamente y recorre:

1. **CREATE** — guarda empleado, cliente, servicio, recurso, usuario y una cita
   con recurso reservado (`save`).
2. **READ** — `count`, `findById` (CRUD heredado).
3. **Queries personalizadas** — métodos derivados y `@Query` JPQL, incluyendo
   las proyecciones a DTO.
4. **UPDATE** — cambia el estado de la cita y lo vuelve a persistir.
5. **DELETE** — elimina la cita (`deleteById`) y confirma que ya no existe.

La demo primero **limpia** los datos previos, por lo que se puede ejecutar varias
veces sin chocar con restricciones `unique`.

> **Detalle de diseño:** `ClienteRepository.findByDocumento` /
> `findByTelefono` no se usan en la demo porque esos campos se guardan cifrados
> (AES/ECB) y la búsqueda derivada compararía el texto plano contra el valor
> cifrado en la DB. Para esos campos la búsqueda se haría cifrando primero el
> parámetro. La demo usa búsquedas sobre campos no cifrados (p. ej. `nombre`).
