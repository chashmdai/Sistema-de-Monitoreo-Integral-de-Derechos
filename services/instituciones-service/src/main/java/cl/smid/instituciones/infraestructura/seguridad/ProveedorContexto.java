package cl.smid.instituciones.infraestructura.seguridad;

import cl.smid.instituciones.dominio.modelo.ContextoSesion;

/**
 * Puerto de lectura del contexto de sesión de la petición en curso. Lo usan los
 * controladores para obtener el {@link ContextoSesion} y pasarlo al dominio en las
 * operaciones de escritura.
 */
public interface ProveedorContexto {

    /**
     * @return el contexto de la petición actual, o {@code null} si no hay sesión.
     */
    ContextoSesion contextoActual();
}
