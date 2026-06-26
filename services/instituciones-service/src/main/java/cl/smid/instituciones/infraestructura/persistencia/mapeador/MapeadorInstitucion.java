package cl.smid.instituciones.infraestructura.persistencia.mapeador;

import cl.smid.instituciones.dominio.modelo.Institucion;
import cl.smid.instituciones.dominio.modelo.Rut;
import cl.smid.instituciones.infraestructura.persistencia.entidad.InstitucionEntity;
import org.springframework.stereotype.Component;

/**
 * Mapeador entre el agregado {@link Institucion} y la entidad JPA
 * {@link InstitucionEntity}. La traducción {@code tipoAlt <-> tipo_id} la provee el
 * adaptador (que conoce el repositorio de tipos); aquí se reciben/entregan ya resueltos.
 * El RUT del dominio (objeto de valor) se descompone en las columnas {@code rut}/{@code dv}.
 */
@Component
public class MapeadorInstitucion {

    /**
     * Reconstruye el agregado de dominio desde la entidad persistida.
     *
     * @param e       entidad persistida
     * @param tipoAlt alt_key del tipo (resuelto por el adaptador a partir de {@code tipo_id})
     * @return el agregado de dominio
     */
    public Institucion aDominio(InstitucionEntity e, String tipoAlt) {
        Rut rut = (e.getRut() != null && e.getDv() != null && !e.getDv().isEmpty())
                ? Rut.reconstruir(e.getRut(), e.getDv().charAt(0))
                : null;
        return Institucion.reconstruir(
                e.getAltKey(),
                e.getCodigo(),
                e.getNombre(),
                tipoAlt,
                rut,
                e.getRegionCodigo(),
                e.getComunaCodigo(),
                e.getDireccion(),
                e.getTelefono(),
                e.getEmail(),
                e.getSitioWeb(),
                e.isActiva(),
                MarcasTiempo.aInstante(e.getCreadoEn()),
                MarcasTiempo.aInstante(e.getActualizadoEn()));
    }

    /**
     * Vuelca el estado del agregado sobre una entidad destino (nueva o existente).
     *
     * @param institucion agregado de dominio
     * @param tipoId      PK interna del tipo (resuelta por el adaptador)
     * @param destino     entidad destino
     */
    public void copiarADestino(Institucion institucion, Long tipoId, InstitucionEntity destino) {
        destino.setAltKey(institucion.altKey());
        destino.setCodigo(institucion.codigo());
        destino.setNombre(institucion.nombre());
        destino.setTipoId(tipoId);
        if (institucion.rut() != null) {
            destino.setRut(institucion.rut().cuerpo());
            destino.setDv(String.valueOf(institucion.rut().dv()));
        } else {
            destino.setRut(null);
            destino.setDv(null);
        }
        destino.setRegionCodigo(institucion.regionCodigo());
        destino.setComunaCodigo(institucion.comunaCodigo());
        destino.setDireccion(institucion.direccion());
        destino.setTelefono(institucion.telefono());
        destino.setEmail(institucion.email());
        destino.setSitioWeb(institucion.sitioWeb());
        destino.setActiva(institucion.activa());
        destino.setCreadoEn(MarcasTiempo.aLocal(institucion.creadoEn()));
        destino.setActualizadoEn(MarcasTiempo.aLocal(institucion.actualizadoEn()));
    }
}
