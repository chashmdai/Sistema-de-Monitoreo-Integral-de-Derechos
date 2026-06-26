package cl.smid.instituciones.infraestructura.persistencia.mapeador;

import cl.smid.instituciones.dominio.modelo.PuntoFocal;
import cl.smid.instituciones.infraestructura.persistencia.entidad.PuntoFocalEntity;
import org.springframework.stereotype.Component;

/**
 * Mapeador entre la entidad {@link PuntoFocal} del dominio y la entidad JPA
 * {@link PuntoFocalEntity}. La traducción {@code institucionAlt <-> institucion_id} la
 * provee el adaptador.
 */
@Component
public class MapeadorPuntoFocal {

    /**
     * Reconstruye la entidad de dominio desde la entidad persistida.
     *
     * @param e              entidad persistida
     * @param institucionAlt alt_key de la institución (resuelto por el adaptador)
     * @return la entidad de dominio
     */
    public PuntoFocal aDominio(PuntoFocalEntity e, String institucionAlt) {
        return PuntoFocal.reconstruir(
                e.getAltKey(),
                institucionAlt,
                e.getNombre(),
                e.getCargo(),
                e.getEmail(),
                e.getTelefono(),
                e.isPrincipal(),
                e.isActivo(),
                MarcasTiempo.aInstante(e.getCreadoEn()),
                MarcasTiempo.aInstante(e.getActualizadoEn()));
    }

    /**
     * Vuelca el estado de la entidad de dominio sobre una entidad destino.
     *
     * @param puntoFocal     entidad de dominio
     * @param institucionId  PK interna de la institución (resuelta por el adaptador)
     * @param destino        entidad destino
     */
    public void copiarADestino(PuntoFocal puntoFocal, Long institucionId, PuntoFocalEntity destino) {
        destino.setAltKey(puntoFocal.altKey());
        destino.setInstitucionId(institucionId);
        destino.setNombre(puntoFocal.nombre());
        destino.setCargo(puntoFocal.cargo());
        destino.setEmail(puntoFocal.email());
        destino.setTelefono(puntoFocal.telefono());
        destino.setPrincipal(puntoFocal.principal());
        destino.setActivo(puntoFocal.activo());
        destino.setCreadoEn(MarcasTiempo.aLocal(puntoFocal.creadoEn()));
        destino.setActualizadoEn(MarcasTiempo.aLocal(puntoFocal.actualizadoEn()));
    }
}
