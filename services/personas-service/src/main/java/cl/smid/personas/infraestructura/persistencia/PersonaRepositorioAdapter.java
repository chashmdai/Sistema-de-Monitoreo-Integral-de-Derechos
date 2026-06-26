package cl.smid.personas.infraestructura.persistencia;

import cl.smid.personas.dominio.modelo.Alcance;
import cl.smid.personas.dominio.modelo.ContextoTerritorial;
import cl.smid.personas.dominio.modelo.Pagina;
import cl.smid.personas.dominio.modelo.Persona;
import cl.smid.personas.dominio.puerto.salida.PersonaRepositorio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Adaptador de salida que implementa {@link PersonaRepositorio} sobre Spring Data JPA. Traduce
 * el alcance territorial del contexto a las banderas que consumen las consultas y mapea entre
 * entidad y dominio en ambos sentidos.
 *
 * <p>Es el único punto donde el alcance se convierte en predicados SQL; la <i>regla</i> de
 * visibilidad sigue viviendo en el dominio ({@code EvaluadorAlcance}), de modo que ambas capas
 * coinciden por construcción.</p>
 */
@Repository
public class PersonaRepositorioAdapter implements PersonaRepositorio {

    /** Límite de candidatos recuperados para la deduplicación difusa ("blocking"). */
    private static final int LIMITE_CANDIDATOS_DEDUP = 200;

    private final PersonaJpaRepository jpa;

    public PersonaRepositorioAdapter(PersonaJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Persona guardar(Persona persona) {
        Objects.requireNonNull(persona, "La persona a guardar no puede ser nula");
        Long id = persona.id();
        PersonaEntity entidad;
        if (id == null) {
            entidad = MapeadorPersona.aEntidadNueva(persona);
        } else {
            // Carga la entidad gestionada y vuelca el estado del dominio sobre ella.
            Long idPersistido = Objects.requireNonNull(id, "El id de una persona persistida no puede ser nulo");
            entidad = jpa.findById(idPersistido)
                    .orElseGet(() -> MapeadorPersona.aEntidadNueva(persona));
            MapeadorPersona.volcarSobre(persona, entidad);
        }
        PersonaEntity guardada = jpa.save(Objects.requireNonNull(entidad, "La entidad a guardar no puede ser nula"));
        return MapeadorPersona.aDominio(guardada);
    }

    @Override
    public Optional<Persona> buscarPorAltKey(String altKey) {
        return jpa.findByAltKey(altKey).map(MapeadorPersona::aDominio);
    }

    @Override
    public Optional<Persona> buscarVigentePorRutGlobal(String rutCanonico) {
        return jpa.findFirstByRutAndVigenteTrue(rutCanonico).map(MapeadorPersona::aDominio);
    }

    @Override
    public Optional<Persona> buscarVigentePorRutEnAlcance(String rutCanonico, ContextoTerritorial ctx) {
        Alcance a = ctx.alcance();
        return jpa.buscarVigentePorRutEnAlcance(
                        rutCanonico,
                        a == Alcance.NACIONAL,
                        a == Alcance.SEDE,
                        a == Alcance.UNIDAD,
                        ctx.idSede(),
                        ctx.idUnidad())
                .map(MapeadorPersona::aDominio);
    }

    @Override
    public Pagina<Persona> buscarPorNombreEnAlcance(String termino, ContextoTerritorial ctx, int pagina, int tamano) {
        Alcance a = ctx.alcance();
        String patron = "%" + (termino == null ? "" : termino.trim()) + "%";
        Pageable pageable = PageRequest.of(Math.max(0, pagina), Math.max(1, tamano));

        Page<PersonaEntity> page = jpa.buscarPorNombreEnAlcance(
                patron,
                a == Alcance.NACIONAL,
                a == Alcance.SEDE,
                a == Alcance.UNIDAD,
                ctx.idSede(),
                ctx.idUnidad(),
                pageable);

        List<Persona> contenido = page.getContent().stream()
                .map(MapeadorPersona::aDominio)
                .toList();
        return new Pagina<>(contenido, page.getNumber(), page.getSize(), page.getTotalElements());
    }

    @Override
    public List<Persona> candidatosParaDedup(String prefijoApellido, LocalDate fechaNacimiento) {
        String prefijoLike = (prefijoApellido == null || prefijoApellido.isBlank())
                ? null
                : prefijoApellido.trim() + "%";
        Pageable limite = PageRequest.of(0, LIMITE_CANDIDATOS_DEDUP);
        return jpa.candidatosParaDedup(prefijoLike, fechaNacimiento, limite).stream()
                .map(MapeadorPersona::aDominio)
                .toList();
    }
}
