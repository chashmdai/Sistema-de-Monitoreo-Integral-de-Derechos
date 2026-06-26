package cl.smid.requerimientos.infraestructura.persistencia.adaptador;

import cl.smid.requerimientos.dominio.modelo.SnapshotPersona;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Convierte un {@link SnapshotPersona} del dominio a/desde su representación JSON persistida en
 * las columnas {@code requirente_snapshot} y {@code persona_snapshot}.
 *
 * <p>Se serializa manualmente a un mapa estable ({@code nombreLegible}, {@code rut},
 * {@code capturadoEn} en ISO-8601) para no depender del flag {@code -parameters} del compilador al
 * deserializar records. El instante se guarda en UTC.</p>
 */
@Component
public class MapeadorSnapshot {

    private static final String CAMPO_NOMBRE = "nombreLegible";
    private static final String CAMPO_RUT = "rut";
    private static final String CAMPO_CAPTURA = "capturadoEn";

    private final ObjectMapper objectMapper;

    public MapeadorSnapshot(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Serializa un snapshot a su cadena JSON.
     *
     * @param snapshot snapshot de dominio (nulable)
     * @return JSON, o {@code null} si el snapshot es nulo
     */
    public String aJson(SnapshotPersona snapshot) {
        if (snapshot == null) {
            return null;
        }
        Map<String, Object> mapa = new LinkedHashMap<>();
        mapa.put(CAMPO_NOMBRE, snapshot.nombreLegible());
        mapa.put(CAMPO_RUT, snapshot.rut());
        mapa.put(CAMPO_CAPTURA, snapshot.capturadoEn() == null ? null : snapshot.capturadoEn().toString());
        try {
            return objectMapper.writeValueAsString(mapa);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo serializar el snapshot de persona", e);
        }
    }

    /**
     * Reconstruye un snapshot desde su cadena JSON.
     *
     * @param json cadena JSON persistida (nulable)
     * @return snapshot de dominio, o {@code null} si el JSON es nulo/vacío
     */
    public SnapshotPersona desdeJson(String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            Map<?, ?> mapa = objectMapper.readValue(json, Map.class);
            String nombre = valorTexto(mapa.get(CAMPO_NOMBRE));
            String rut = valorTexto(mapa.get(CAMPO_RUT));
            Object capturaRaw = mapa.get(CAMPO_CAPTURA);
            Instant captura = (capturaRaw == null) ? null : Instant.parse(capturaRaw.toString());
            return new SnapshotPersona(nombre, rut, captura);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo deserializar el snapshot de persona", e);
        }
    }

    private String valorTexto(Object valor) {
        return (valor == null) ? null : valor.toString();
    }
}
