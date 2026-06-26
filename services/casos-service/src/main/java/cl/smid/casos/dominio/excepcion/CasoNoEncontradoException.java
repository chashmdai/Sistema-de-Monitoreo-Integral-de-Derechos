package cl.smid.casos.dominio.excepcion;

/**
 * El caso no existe O está fuera del alcance territorial del solicitante. En ambos casos se responde
 * {@code 404} (CAS-404), nunca {@code 403}, para no revelar la existencia del recurso (Núcleo 2.3).
 */
public class CasoNoEncontradoException extends ExcepcionDominio {

    public CasoNoEncontradoException(String altKey) {
        super(CodigoError.CAS_404, "No existe un caso accesible con el identificador indicado: " + altKey);
    }
}
