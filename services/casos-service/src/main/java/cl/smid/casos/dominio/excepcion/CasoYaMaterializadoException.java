package cl.smid.casos.dominio.excepcion;

/**
 * Señala que ya existe un caso para el requerimiento de origen indicado. Es la traducción de dominio
 * de la violación de la clave única {@code id_requerimiento_origen_alt}.
 *
 * <p>Sostiene la idempotencia del listener (entrega <em>at-least-once</em>): ante la reentrega de un
 * mismo evento, el servicio captura esta excepción y la trata como <strong>no-op</strong> (ack
 * normal, sin error y sin crear un segundo caso).</p>
 */
public class CasoYaMaterializadoException extends ExcepcionDominio {

    private final transient String requerimientoOrigenAlt;

    public CasoYaMaterializadoException(String requerimientoOrigenAlt) {
        super(CodigoError.CAS_409,
                "Ya existe un caso para el requerimiento de origen " + requerimientoOrigenAlt + ".");
        this.requerimientoOrigenAlt = requerimientoOrigenAlt;
    }

    public String requerimientoOrigenAlt() {
        return requerimientoOrigenAlt;
    }
}
