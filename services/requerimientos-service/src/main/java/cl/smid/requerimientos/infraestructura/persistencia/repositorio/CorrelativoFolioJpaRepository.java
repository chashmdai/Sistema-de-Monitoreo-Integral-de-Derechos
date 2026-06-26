package cl.smid.requerimientos.infraestructura.persistencia.repositorio;

import cl.smid.requerimientos.infraestructura.persistencia.entidad.CorrelativoFolioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio Spring Data del contador correlativo de folio.
 *
 * <p>La reserva del correlativo se hace con un <b>UPSERT atómico</b> de MySQL
 * ({@code INSERT ... ON DUPLICATE KEY UPDATE}): si la fila (sede, año, serie) no existe se inserta
 * en 1; si existe, se incrementa en 1. La operación toma un bloqueo exclusivo de la fila que se
 * mantiene hasta el commit, de modo que solicitudes concurrentes para la misma clave se serializan
 * y nunca obtienen el mismo número. Como una transacción siempre observa sus propias escrituras, la
 * lectura posterior devuelve exactamente el valor reservado por esta transacción.</p>
 */
public interface CorrelativoFolioJpaRepository extends JpaRepository<CorrelativoFolioEntity, Long> {

    /**
     * Inserta la fila del contador en 1 o, si ya existe, incrementa su valor en 1 (atómico).
     *
     * @param idSede alt_key de la sede
     * @param anio   año de la serie
     * @param serie  serie (OFICIAL/BETA)
     */
    @Modifying
    @Query(value = """
            INSERT INTO correlativo_folio (id_sede, anio, serie, ultimo_valor)
            VALUES (:idSede, :anio, :serie, 1)
            ON DUPLICATE KEY UPDATE ultimo_valor = ultimo_valor + 1
            """, nativeQuery = true)
    void incrementarOInsertar(@Param("idSede") String idSede,
                              @Param("anio") int anio,
                              @Param("serie") String serie);

    /**
     * Lee el valor actual del contador para (sede, año, serie).
     *
     * @param idSede alt_key de la sede
     * @param anio   año de la serie
     * @param serie  serie (OFICIAL/BETA)
     * @return el último valor reservado
     */
    @Query(value = """
            SELECT ultimo_valor FROM correlativo_folio
            WHERE id_sede = :idSede AND anio = :anio AND serie = :serie
            """, nativeQuery = true)
    Long leerValor(@Param("idSede") String idSede,
                   @Param("anio") int anio,
                   @Param("serie") String serie);
}
