package cl.smid.requerimientos.dominio.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * NNA (niño, niña o adolescente) afectado por un requerimiento (Núcleo 6.2).
 *
 * <p>Solo persiste el {@code alt_key} del NNA en personas-service y un {@link SnapshotPersona}
 * mínimo para resiliencia de lectura. Agrupa los derechos vulnerados imputados.</p>
 *
 * <p>Entidad mutable dentro del agregado {@link Requerimiento} (se le agregan derechos).
 * Pertenece al dominio puro.</p>
 */
public class NnaAfectado {

    /** Handle de persistencia opaco; nulo para líneas nuevas. Jamás cruza la API. */
    private final Long idInterno;
    /** alt_key del NNA en personas-service. */
    private final String idPersonaAlt;
    /** Snapshot mínimo (nombre/RUT) tomado al asociar. */
    private final SnapshotPersona snapshot;
    /** Derechos vulnerados imputados a este NNA. */
    private final List<DerechoVulnerado> derechos;

    /**
     * Constructor de rehidratación (carga desde persistencia).
     *
     * @param idInterno    handle de persistencia (puede ser nulo)
     * @param idPersonaAlt alt_key del NNA
     * @param snapshot     snapshot mínimo (puede ser nulo)
     * @param derechos     derechos ya asociados (puede ser nulo o vacío)
     */
    public NnaAfectado(Long idInterno, String idPersonaAlt, SnapshotPersona snapshot,
                       List<DerechoVulnerado> derechos) {
        this.idInterno = idInterno;
        this.idPersonaAlt = Objects.requireNonNull(idPersonaAlt, "El alt_key del NNA es obligatorio");
        if (idPersonaAlt.isBlank()) {
            throw new IllegalArgumentException("El alt_key del NNA no puede estar en blanco");
        }
        this.snapshot = snapshot;
        this.derechos = (derechos == null) ? new ArrayList<>() : new ArrayList<>(derechos);
    }

    /**
     * Crea un NNA afectado nuevo (sin persistir).
     *
     * @param idPersonaAlt alt_key del NNA
     * @param snapshot     snapshot mínimo
     * @param derechos     derechos a imputar (puede ser nulo)
     * @return el NNA afectado
     */
    public static NnaAfectado nuevo(String idPersonaAlt, SnapshotPersona snapshot,
                                    List<DerechoVulnerado> derechos) {
        return new NnaAfectado(null, idPersonaAlt, snapshot, derechos);
    }

    /**
     * Agrega un derecho vulnerado a este NNA.
     *
     * @param derecho la línea de derecho a agregar
     */
    public void agregarDerecho(DerechoVulnerado derecho) {
        this.derechos.add(Objects.requireNonNull(derecho, "El derecho no puede ser nulo"));
    }

    /** @return {@code true} si el NNA aún no se ha persistido. */
    public boolean esNuevo() {
        return idInterno == null;
    }

    public Long idInterno() {
        return idInterno;
    }

    public String idPersonaAlt() {
        return idPersonaAlt;
    }

    public SnapshotPersona snapshot() {
        return snapshot;
    }

    /** @return vista inmodificable de los derechos imputados. */
    public List<DerechoVulnerado> derechos() {
        return Collections.unmodifiableList(derechos);
    }
}
