package cl.smid.personas.infraestructura.persistencia;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data de {@link PersonaEntity}. Expone las consultas que el adaptador del
 * puerto de dominio necesita: obtención por alt_key, unicidad/búsqueda por RUT (global y
 * territorial), búsqueda paginada por nombre con filtro de alcance, y recuperación de candidatos
 * para la deduplicación difusa ("blocking").
 *
 * <p>El filtro territorial se resuelve con banderas booleanas ({@code nacional/porSede/porUnidad})
 * para evitar tres variantes de método y mantener una única consulta cuya semántica coincide con
 * {@code EvaluadorAlcance}. La colación {@code utf8mb4_0900_ai_ci} hace que los {@code LIKE} sean
 * insensibles a tildes y mayúsculas sin funciones adicionales.</p>
 */
public interface PersonaJpaRepository extends JpaRepository<PersonaEntity, Long> {

    /** Obtención por identificador público (sin filtro territorial; el dominio decide visibilidad). */
    Optional<PersonaEntity> findByAltKey(String altKey);

    /** Persona vigente por RUT canónico en toda la base (unicidad global y match exacto de dedup). */
    Optional<PersonaEntity> findFirstByRutAndVigenteTrue(String rut);

    /**
     * Persona vigente por RUT dentro del alcance territorial del solicitante.
     */
    @Query("""
            select p from PersonaEntity p
            where p.vigente = true
              and p.rut = :rut
              and ( :nacional = true
                    or (:porSede = true and p.idSede = :idSede)
                    or (:porUnidad = true and p.idUnidad = :idUnidad) )
            """)
    Optional<PersonaEntity> buscarVigentePorRutEnAlcance(
            @Param("rut") String rut,
            @Param("nacional") boolean nacional,
            @Param("porSede") boolean porSede,
            @Param("porUnidad") boolean porUnidad,
            @Param("idSede") String idSede,
            @Param("idUnidad") String idUnidad);

    /**
     * Búsqueda paginada de personas vigentes por término de nombre (coincidencia parcial sobre
     * nombres, apellidos o razón social), filtrada por alcance territorial.
     */
    @Query(value = """
            select p from PersonaEntity p
            where p.vigente = true
              and ( :nacional = true
                    or (:porSede = true and p.idSede = :idSede)
                    or (:porUnidad = true and p.idUnidad = :idUnidad) )
              and ( p.nombres like :patron
                    or p.apellidoPaterno like :patron
                    or p.apellidoMaterno like :patron
                    or p.razonSocial like :patron
                    or concat(coalesce(p.nombres,''), ' ', coalesce(p.apellidoPaterno,'')) like :patron )
            """,
            countQuery = """
            select count(p) from PersonaEntity p
            where p.vigente = true
              and ( :nacional = true
                    or (:porSede = true and p.idSede = :idSede)
                    or (:porUnidad = true and p.idUnidad = :idUnidad) )
              and ( p.nombres like :patron
                    or p.apellidoPaterno like :patron
                    or p.apellidoMaterno like :patron
                    or p.razonSocial like :patron
                    or concat(coalesce(p.nombres,''), ' ', coalesce(p.apellidoPaterno,'')) like :patron )
            """)
    Page<PersonaEntity> buscarPorNombreEnAlcance(
            @Param("patron") String patron,
            @Param("nacional") boolean nacional,
            @Param("porSede") boolean porSede,
            @Param("porUnidad") boolean porUnidad,
            @Param("idSede") String idSede,
            @Param("idUnidad") String idUnidad,
            Pageable pageable);

    /**
     * Candidatos para la deduplicación difusa ("blocking"): personas vigentes que comparten la
     * fecha de nacimiento exacta <b>o</b> el prefijo de apellido paterno. Cross-territorial por
     * diseño. Si ambos parámetros son nulos, la condición es falsa para todos y no devuelve filas.
     *
     * @param prefijo prefijo de apellido con comodín final (p. ej. {@code "per%"}) o nulo
     * @param fecha   fecha de nacimiento exacta o nula
     */
    @Query("""
            select p from PersonaEntity p
            where p.vigente = true
              and ( (:fecha is not null and p.fechaNacimiento = :fecha)
                    or (:prefijo is not null and p.apellidoPaterno like :prefijo) )
            """)
    List<PersonaEntity> candidatosParaDedup(
            @Param("prefijo") String prefijo,
            @Param("fecha") LocalDate fecha,
            Pageable limite);
}
