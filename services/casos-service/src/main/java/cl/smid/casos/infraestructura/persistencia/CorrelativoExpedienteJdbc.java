package cl.smid.casos.infraestructura.persistencia;

import cl.smid.casos.dominio.modelo.SerieExpediente;
import cl.smid.casos.dominio.puerto.salida.CorrelativoExpedientePort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Adaptador del correlativo de expediente, atómico y seguro ante concurrencia. Reutiliza el patrón ya
 * probado en requerimientos: un UPSERT de MySQL combinado con {@code LAST_INSERT_ID()} que serializa
 * las solicitudes concurrentes bajo bloqueo de fila y entrega un número exclusivo a cada llamante.
 *
 * <p>Clave de la técnica: el UPSERT y el {@code SELECT LAST_INSERT_ID()} deben ejecutarse sobre la
 * <strong>misma conexión</strong> (el valor de {@code LAST_INSERT_ID} es por conexión). Por eso se usa
 * {@link JdbcTemplate#execute(org.springframework.jdbc.core.ConnectionCallback)}: así, cuando el
 * método corre dentro de la transacción del listener, ambas sentencias comparten la conexión de esa
 * transacción y el correlativo participa del mismo commit/rollback (sin huecos ante rollback).</p>
 *
 * <pre>
 *   INSERT INTO correlativo_expediente (id_sede_alt, anio, serie, ultimo)
 *   VALUES (?, ?, ?, LAST_INSERT_ID(1))
 *   ON DUPLICATE KEY UPDATE ultimo = LAST_INSERT_ID(ultimo + 1);
 *   SELECT LAST_INSERT_ID();
 * </pre>
 *
 * <p>{@code LAST_INSERT_ID(1)} hace que la PRIMERA inserción de una serie devuelva 1; en colisiones de
 * clave, {@code LAST_INSERT_ID(ultimo + 1)} incrementa y publica el nuevo valor en la conexión.</p>
 */
@Repository
public class CorrelativoExpedienteJdbc implements CorrelativoExpedientePort {

    private static final String SQL_UPSERT = """
            INSERT INTO correlativo_expediente (id_sede_alt, anio, serie, ultimo)
            VALUES (?, ?, ?, LAST_INSERT_ID(1))
            ON DUPLICATE KEY UPDATE ultimo = LAST_INSERT_ID(ultimo + 1)
            """;

    private static final String SQL_LEER = "SELECT LAST_INSERT_ID()";

    private final JdbcTemplate jdbcTemplate;

    public CorrelativoExpedienteJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long reservarSiguiente(String idSedeAlt, int anio, SerieExpediente serie) {
        Long valor = jdbcTemplate.execute((java.sql.Connection conexion) -> {
            try (PreparedStatement upsert = conexion.prepareStatement(SQL_UPSERT)) {
                upsert.setString(1, idSedeAlt);
                upsert.setInt(2, anio);
                upsert.setString(3, serie.name());
                upsert.executeUpdate();
            }
            try (PreparedStatement lectura = conexion.prepareStatement(SQL_LEER);
                 ResultSet rs = lectura.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                throw new SQLException("No se pudo obtener el correlativo de expediente reservado.");
            }
        });
        if (valor == null || valor <= 0) {
            throw new IllegalStateException("Reserva de correlativo de expediente inválida.");
        }
        return valor;
    }
}
