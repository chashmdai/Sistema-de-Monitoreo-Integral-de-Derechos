package cl.smid.requerimientos.dominio.puerto.salida;

import cl.smid.requerimientos.dominio.modelo.SerieFolio;

/**
 * Puerto de salida del contador correlativo de folio (Núcleo 6.5). Garantiza unicidad bajo
 * concurrencia: la implementación incrementa de forma atómica (bloqueo pesimista
 * {@code SELECT ... FOR UPDATE} o UPSERT) dentro de la transacción del controlador, de modo
 * que dos solicitudes concurrentes para la misma (sede, año, serie) nunca obtengan el mismo
 * número.
 *
 * <p>La serie {@link SerieFolio#BETA} corre por una fila independiente: no consume la
 * numeración {@link SerieFolio#OFICIAL}.</p>
 */
public interface CorrelativoFolioRepositorio {

    /**
     * Reserva y devuelve el siguiente correlativo para la combinación (sede, año, serie),
     * incrementándolo de forma atómica. La primera reserva del año para una serie devuelve 1
     * (la fila nace en 0 y se incrementa antes de leer).
     *
     * @param idSedeAlt alt_key de la sede
     * @param anio      año de la serie
     * @param serie     serie de numeración (OFICIAL o BETA)
     * @return el correlativo reservado (&gt;= 1), único para esa (sede, año, serie)
     */
    long siguienteCorrelativo(String idSedeAlt, int anio, SerieFolio serie);
}
