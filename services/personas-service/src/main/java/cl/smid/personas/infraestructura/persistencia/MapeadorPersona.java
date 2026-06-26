package cl.smid.personas.infraestructura.persistencia;

import cl.smid.personas.dominio.modelo.Contacto;
import cl.smid.personas.dominio.modelo.Persona;
import cl.smid.personas.dominio.modelo.Sexo;
import cl.smid.personas.dominio.modelo.TipoContacto;
import cl.smid.personas.dominio.modelo.TipoPersona;

import java.util.ArrayList;
import java.util.List;

/**
 * Traduce entre la entidad JPA {@link PersonaEntity} (infraestructura) y el agregado de dominio
 * {@link Persona} (núcleo puro). Mantener esta frontera explícita evita que las anotaciones de
 * persistencia contaminen el dominio y permite que cada lado evolucione por separado.
 *
 * <p>Los enums se materializan como texto en la base; aquí se convierten desde/hacia sus tipos de
 * dominio. Como el esquema ya restringe los valores con {@code CHECK}, la conversión es segura.</p>
 */
public final class MapeadorPersona {

    private MapeadorPersona() {
        // Clase de utilidades: no instanciable.
    }

    /** Entidad → dominio. */
    public static Persona aDominio(PersonaEntity e) {
        if (e == null) {
            return null;
        }
        List<Contacto> contactos = new ArrayList<>();
        if (e.getContactos() != null) {
            for (PersonaContactoEntity c : e.getContactos()) {
                contactos.add(new Contacto(TipoContacto.valueOf(c.getTipo()), c.getValor()));
            }
        }
        return Persona.builder()
                .id(e.getId())
                .altKey(e.getAltKey())
                .tipo(e.getTipo() == null ? null : TipoPersona.valueOf(e.getTipo()))
                .rut(e.getRut())
                .dv(e.getDv())
                .nombres(e.getNombres())
                .apellidoPaterno(e.getApellidoPaterno())
                .apellidoMaterno(e.getApellidoMaterno())
                .razonSocial(e.getRazonSocial())
                .fechaNacimiento(e.getFechaNacimiento())
                .sexo(e.getSexo() == null ? null : Sexo.valueOf(e.getSexo()))
                .nacionalidad(e.getNacionalidad())
                .hashDedup(e.getHashDedup())
                .idSede(e.getIdSede())
                .idUnidad(e.getIdUnidad())
                .vigente(e.isVigente())
                .creadoEn(e.getCreadoEn())
                .actualizadoEn(e.getActualizadoEn())
                .creadoPor(e.getCreadoPor())
                .contactos(contactos)
                .construir();
    }

    /**
     * Dominio → entidad nueva (para alta). No fija {@code id} (lo asigna el motor).
     */
    public static PersonaEntity aEntidadNueva(Persona p) {
        PersonaEntity e = new PersonaEntity();
        copiarCampos(p, e);
        return e;
    }

    /**
     * Vuelca el estado del dominio sobre una entidad ya gestionada (para actualización),
     * preservando su identidad y reemplazando los contactos de forma coherente.
     */
    public static void volcarSobre(Persona p, PersonaEntity e) {
        copiarCampos(p, e);
    }

    /** Copia los campos del agregado a la entidad, incluyendo el reemplazo de contactos. */
    private static void copiarCampos(Persona p, PersonaEntity e) {
        e.setAltKey(p.altKey());
        e.setTipo(p.tipo() == null ? null : p.tipo().name());
        e.setRut(p.rut());
        e.setDv(p.dv());
        e.setNombres(p.nombres());
        e.setApellidoPaterno(p.apellidoPaterno());
        e.setApellidoMaterno(p.apellidoMaterno());
        e.setRazonSocial(p.razonSocial());
        e.setFechaNacimiento(p.fechaNacimiento());
        e.setSexo(p.sexo() == null ? null : p.sexo().name());
        e.setNacionalidad(p.nacionalidad());
        e.setHashDedup(p.hashDedup());
        e.setIdSede(p.idSede());
        e.setIdUnidad(p.idUnidad());
        e.setVigente(p.vigente());
        e.setCreadoEn(p.creadoEn());
        e.setActualizadoEn(p.actualizadoEn());
        e.setCreadoPor(p.creadoPor());

        List<PersonaContactoEntity> contactos = new ArrayList<>();
        if (p.contactos() != null) {
            for (Contacto c : p.contactos()) {
                contactos.add(PersonaContactoEntity.builder()
                        .tipo(c.tipo().name())
                        .valor(c.valor())
                        .build());
            }
        }
        e.reemplazarContactos(contactos);
    }
}
