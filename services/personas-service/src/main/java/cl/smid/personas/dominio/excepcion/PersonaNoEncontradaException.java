package cl.smid.personas.dominio.excepcion;

/**
 * Se lanza cuando la persona solicitada no existe <b>o</b> está fuera del alcance
 * territorial del solicitante (§2.3). En ambos casos la respuesta es la misma —HTTP 404,
 * código {@code PER-404}— para no revelar la existencia de registros fuera de alcance.
 */
public class PersonaNoEncontradaException extends PersonaException {

    public PersonaNoEncontradaException(String altKey) {
        super(CodigoError.PERSONA_NO_ENCONTRADA,
                "No se encontró la persona '" + altKey + "' o está fuera de su alcance.");
    }
}
