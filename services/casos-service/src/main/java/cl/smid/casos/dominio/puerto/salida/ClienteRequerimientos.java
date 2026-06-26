package cl.smid.casos.dominio.puerto.salida;

import cl.smid.casos.dominio.modelo.DatosEnriquecimiento;

/**
 * Puerto de salida para el enriquecimiento on-demand de un caso contra requerimientos-service (6.3).
 *
 * <p>Es una COSTURA: la materialización inicial es un esqueleto; el detalle se resuelve al consultar
 * el caso, CON EL TOKEN DEL USUARIO (propagación estándar, respetando territorial y G7). El cruce es
 * tolerante: si el servicio de origen no responde o el enriquecimiento está desactivado, devuelve
 * {@link DatosEnriquecimiento#noDisponible()} y el caso se entrega igual (esqueleto).</p>
 */
public interface ClienteRequerimientos {

    /**
     * Resuelve metadatos no sensibles del requerimiento de origen.
     *
     * @param requerimientoOrigenAlt alt_key del requerimiento de origen.
     * @param tokenBearer            token del usuario solicitante (sin el prefijo "Bearer ").
     * @return datos de enriquecimiento, o {@link DatosEnriquecimiento#noDisponible()} si no aplica.
     */
    DatosEnriquecimiento enriquecer(String requerimientoOrigenAlt, String tokenBearer);
}
