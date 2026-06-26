package cl.smid.requerimientos.dominio.puerto.salida;

import cl.smid.requerimientos.dominio.modelo.Alcance;
import cl.smid.requerimientos.dominio.modelo.EstadoRequerimiento;
import cl.smid.requerimientos.dominio.modelo.PaginaDominio;
import cl.smid.requerimientos.dominio.modelo.Requerimiento;

import java.util.Optional;

/**
 * Puerto de salida del agregado {@link Requerimiento} (Núcleo 6.3). El dominio define el
 * contrato de persistencia; la infraestructura lo implementa con JPA/MySQL. El dominio no
 * conoce JPA ni SQL.
 */
public interface RequerimientoRepositorio {

    /**
     * Persiste un requerimiento (alta o actualización). Si el agregado no tiene PK interna se
     * inserta y se le asigna; si la tiene, se actualiza. Devuelve el agregado rehidratado con
     * los handles de persistencia ya resueltos.
     *
     * @param requerimiento agregado a persistir
     * @return el agregado persistido (con {@code idInterno} y handles de hijos resueltos)
     */
    Requerimiento guardar(Requerimiento requerimiento);

    /**
     * Busca un requerimiento vigente por su alt_key público.
     *
     * @param altKey alt_key del requerimiento
     * @return el agregado, o vacío si no existe (o no está vigente)
     */
    Optional<Requerimiento> buscarPorAltKey(String altKey);

    /**
     * Lista requerimientos vigentes aplicando filtros opcionales y el alcance territorial del
     * usuario (registro a registro, Núcleo 2.3).
     *
     * @param estado          filtro opcional por estado (nulo = todos)
     * @param unidadDestinoAlt filtro opcional por unidad de destino (nulo = todas)
     * @param alcance         alcance territorial del usuario
     * @param sedeUsuarioAlt  alt_key de la sede del usuario (para alcance SEDE)
     * @param unidadUsuarioAlt alt_key de la unidad del usuario (para alcance UNIDAD)
     * @param pagina          índice de página (base 0)
     * @param tamano          tamaño de página
     * @return página de requerimientos visibles que satisfacen los filtros
     */
    PaginaDominio<Requerimiento> listar(EstadoRequerimiento estado,
                                        String unidadDestinoAlt,
                                        Alcance alcance,
                                        String sedeUsuarioAlt,
                                        String unidadUsuarioAlt,
                                        int pagina,
                                        int tamano);
}
