package cl.smid.instituciones.dominio.excepcion;

/**
 * Raíz de la jerarquía de excepciones de negocio del dominio.
 *
 * <p>Convención del ecosistema: constructor con <strong>solo</strong> el mensaje y
 * un {@link CodigoError} fijo por subclase (método abstracto {@link #codigo()}).
 * El dominio no conoce HTTP; el mapeo a estado/sobre lo realiza la capa API.</p>
 */
public abstract class ExcepcionDominio extends RuntimeException {

    protected ExcepcionDominio(String mensaje) {
        super(mensaje);
    }

    /** @return el código de error fijo de esta clase de excepción. */
    public abstract CodigoError codigo();
}
