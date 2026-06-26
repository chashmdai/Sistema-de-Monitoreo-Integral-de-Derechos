package cl.smid.antecedentes.infraestructura.persistencia;

import cl.smid.antecedentes.dominio.puerto.salida.CorrelativoFolioPort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

/**
 * Reserva atomica de correlativos sobre la tabla {@code correlativo_folio} usando el modismo
 * MySQL:
 *
 * <pre>
 *   INSERT INTO correlativo_folio (serie, anio, ultimo_valor)
 *   VALUES (:serie, :anio, LAST_INSERT_ID(1))
 *   ON DUPLICATE KEY UPDATE ultimo_valor = LAST_INSERT_ID(ultimo_valor + 1);
 *   SELECT LAST_INSERT_ID();
 * </pre>
 *
 * <p>El truco {@code LAST_INSERT_ID(1)} en la rama de insercion fija el valor de sesion a 1, de
 * modo que {@code SELECT LAST_INSERT_ID()} devuelve el correlativo tanto en alta como en
 * actualizacion. InnoDB serializa las solicitudes concurrentes bajo bloqueo de la fila de la
 * serie, garantizando correlativos exclusivos. Nunca usa {@code MAX()+1}. Se ejecuta sobre la
 * conexion del {@link EntityManager}, dentro de la transaccion del controlador.</p>
 */
@Component
public class AdaptadorCorrelativoFolio implements CorrelativoFolioPort {

    @PersistenceContext
    private EntityManager em;

    @Override
    public long siguiente(String serie, int anio) {
        em.createNativeQuery("""
                        INSERT INTO correlativo_folio (serie, anio, ultimo_valor)
                        VALUES (:serie, :anio, LAST_INSERT_ID(1))
                        ON DUPLICATE KEY UPDATE ultimo_valor = LAST_INSERT_ID(ultimo_valor + 1)
                        """)
                .setParameter("serie", serie)
                .setParameter("anio", anio)
                .executeUpdate();
        Number valor = (Number) em.createNativeQuery("SELECT LAST_INSERT_ID()").getSingleResult();
        return valor.longValue();
    }
}
