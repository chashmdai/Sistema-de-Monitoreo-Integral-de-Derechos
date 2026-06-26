package cl.smid.personas.api;

import cl.smid.personas.api.dto.ActualizarPersonaRequest;
import cl.smid.personas.api.dto.BuscarDuplicadosRequest;
import cl.smid.personas.api.dto.ContactoDTO;
import cl.smid.personas.api.dto.CrearPersonaRequest;
import cl.smid.personas.dominio.excepcion.SolicitudInvalidaException;
import cl.smid.personas.dominio.modelo.Contacto;
import cl.smid.personas.dominio.modelo.CriterioDuplicados;
import cl.smid.personas.dominio.modelo.DatosPersona;
import cl.smid.personas.dominio.modelo.Sexo;
import cl.smid.personas.dominio.modelo.TipoContacto;
import cl.smid.personas.dominio.modelo.TipoPersona;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Traduce los DTOs de entrada a los objetos de dominio (Núcleo 5.4). Aquí se resuelven los
 * valores enumerados que llegan como texto: un valor desconocido produce un error de validación
 * controlado {@code PER-001} (con el campo señalado en {@code detalles}), en lugar de una
 * excepción de deserialización opaca.
 *
 * <p>Clase de utilidades sin estado: métodos estáticos y puros.</p>
 */
public final class MapeadorPeticion {

    private MapeadorPeticion() {
        // Clase de utilidades: no instanciable.
    }

    /** Convierte la petición de alta a los datos de dominio. */
    public static DatosPersona aDatos(CrearPersonaRequest req) {
        TipoPersona tipo = parseTipoPersona(req.tipo(), "tipo");
        Sexo sexo = parseSexo(req.sexo(), "sexo");
        List<Contacto> contactos = parseContactos(req.contactos());

        return new DatosPersona(
                tipo,
                req.rut(),
                req.nombres(),
                req.apellidoPaterno(),
                req.apellidoMaterno(),
                req.razonSocial(),
                req.fechaNacimiento(),
                sexo,
                req.nacionalidad(),
                contactos);
    }

    /**
     * Convierte la petición de actualización a los datos de dominio. El {@code tipo} se deja nulo
     * a propósito: la edición no modifica el tipo de persona. La semántica nula/no-nula de cada
     * campo (partial-merge) la respeta el servicio.
     */
    public static DatosPersona aDatos(ActualizarPersonaRequest req) {
        Sexo sexo = parseSexo(req.sexo(), "sexo");
        List<Contacto> contactos = parseContactos(req.contactos());

        return new DatosPersona(
                null,
                req.rut(),
                req.nombres(),
                req.apellidoPaterno(),
                req.apellidoMaterno(),
                req.razonSocial(),
                req.fechaNacimiento(),
                sexo,
                req.nacionalidad(),
                contactos);
    }

    /** Convierte la petición de prevalidación de duplicados al criterio de dominio. */
    public static CriterioDuplicados aCriterio(BuscarDuplicadosRequest req) {
        TipoPersona tipo = parseTipoPersona(req.tipo(), "tipo");
        return new CriterioDuplicados(
                tipo,
                req.rut(),
                req.nombres(),
                req.apellidoPaterno(),
                req.apellidoMaterno(),
                req.fechaNacimiento());
    }

    // ----------------------------- Parseo de enumerados -----------------------------

    private static TipoPersona parseTipoPersona(String valor, String campo) {
        try {
            return TipoPersona.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SolicitudInvalidaException(
                    "El tipo de persona '" + valor + "' no es válido.",
                    Map.of(campo, "Valores admitidos: NNA, ADULTO, JURIDICA, TESTIGO"));
        }
    }

    /** El sexo es opcional: nulo o en blanco devuelve {@code null}; un valor no reconocido falla. */
    private static Sexo parseSexo(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return Sexo.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new SolicitudInvalidaException(
                    "El sexo '" + valor + "' no es válido.",
                    Map.of(campo, "Valores admitidos: F, M, OTRO, NO_INFORMA"));
        }
    }

    /** Convierte la lista de contactos; nula se mantiene nula (semántica de merge en el servicio). */
    private static List<Contacto> parseContactos(List<ContactoDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        List<Contacto> contactos = new ArrayList<>(dtos.size());
        for (int i = 0; i < dtos.size(); i++) {
            ContactoDTO dto = dtos.get(i);
            TipoContacto tipo = parseTipoContacto(dto.tipo(), "contactos[" + i + "].tipo");
            // El propio record Contacto valida que el valor no esté en blanco.
            contactos.add(new Contacto(tipo, dto.valor()));
        }
        return contactos;
    }

    private static TipoContacto parseTipoContacto(String valor, String campo) {
        try {
            return TipoContacto.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SolicitudInvalidaException(
                    "El tipo de contacto '" + valor + "' no es válido.",
                    Map.of(campo, "Valores admitidos: TELEFONO, EMAIL, DIRECCION"));
        }
    }
}
