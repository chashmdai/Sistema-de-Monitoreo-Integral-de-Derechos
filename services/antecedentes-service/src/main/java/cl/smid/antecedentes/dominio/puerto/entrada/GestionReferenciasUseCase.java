package cl.smid.antecedentes.dominio.puerto.entrada;

import cl.smid.antecedentes.dominio.modelo.ContextoSesion;
import cl.smid.antecedentes.dominio.modelo.Pagina;
import cl.smid.antecedentes.dominio.modelo.Referencia;
import cl.smid.antecedentes.dominio.modelo.TipoReferencia;

/**
 * Caso de uso CRUD de tablas de referencia locales (categorias, procesos, instrumentos).
 * Lectura nacional autenticada; escritura por rol administrador.
 */
public interface GestionReferenciasUseCase {

    /** Crea una referencia (rol admin). */
    Referencia crear(TipoReferencia tipo, ComandoCrear comando, ContextoSesion contexto);

    /** Obtiene una referencia por alt_key (lectura nacional). 404 si no existe. */
    Referencia obtener(TipoReferencia tipo, String altKey, ContextoSesion contexto);

    /** Edita el nombre de una referencia (rol admin). */
    Referencia editar(TipoReferencia tipo, String altKey, ComandoEditar comando, ContextoSesion contexto);

    /** Activa o desactiva una referencia (rol admin). */
    Referencia cambiarVigencia(TipoReferencia tipo, String altKey, ComandoVigencia comando, ContextoSesion contexto);

    /** Lista referencias de un tipo (lectura nacional). */
    Pagina<Referencia> listar(TipoReferencia tipo, String texto, Boolean vigente, int pagina, int tamano,
                              ContextoSesion contexto);

    // -----------------------------------------------------------------------------------
    // Comandos
    // -----------------------------------------------------------------------------------

    /** @param codigo codigo unico estable; @param nombre nombre legible. */
    record ComandoCrear(String codigo, String nombre) {
    }

    /** @param nombre nuevo nombre (el codigo es inmutable). */
    record ComandoEditar(String nombre) {
    }

    /** @param vigente nueva vigencia. */
    record ComandoVigencia(boolean vigente) {
    }
}
