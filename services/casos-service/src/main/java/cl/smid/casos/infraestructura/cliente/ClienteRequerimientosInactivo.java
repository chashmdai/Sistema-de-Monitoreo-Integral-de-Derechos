package cl.smid.casos.infraestructura.cliente;

import cl.smid.casos.dominio.modelo.DatosEnriquecimiento;
import cl.smid.casos.dominio.puerto.salida.ClienteRequerimientos;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Implementación por defecto del enriquecimiento: DESACTIVADA. Devuelve siempre "no disponible", de
 * modo que el caso se entrega como esqueleto. Es la opción segura mientras el contrato de consulta de
 * requerimientos (6.3) no esté habilitado en este despliegue.
 *
 * <p>Activa cuando {@code smid.enriquecimiento.activo} es {@code false} o no está definido.</p>
 */
@Component
@ConditionalOnProperty(name = "smid.enriquecimiento.activo", havingValue = "false", matchIfMissing = true)
public class ClienteRequerimientosInactivo implements ClienteRequerimientos {

    @Override
    public DatosEnriquecimiento enriquecer(String requerimientoOrigenAlt, String tokenBearer) {
        return DatosEnriquecimiento.noDisponible();
    }
}
