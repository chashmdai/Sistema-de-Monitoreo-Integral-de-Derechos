package cl.smid.instituciones.infraestructura.persistencia.mapeador;

import cl.smid.instituciones.dominio.modelo.Ambito;
import cl.smid.instituciones.dominio.modelo.TipoInstitucion;
import cl.smid.instituciones.infraestructura.persistencia.entidad.TipoInstitucionEntity;
import org.springframework.stereotype.Component;

/**
 * Mapeador entre el agregado {@link TipoInstitucion} y la entidad JPA
 * {@link TipoInstitucionEntity}.
 */
@Component
public class MapeadorTipo {

    /**
     * Reconstruye el agregado de dominio desde la entidad persistida.
     */
    public TipoInstitucion aDominio(TipoInstitucionEntity e) {
        return TipoInstitucion.reconstruir(
                e.getAltKey(),
                e.getNombre(),
                Ambito.valueOf(e.getAmbito()),
                e.getDescripcion(),
                e.isVigente(),
                MarcasTiempo.aInstante(e.getCreadoEn()),
                MarcasTiempo.aInstante(e.getActualizadoEn()));
    }

    /**
     * Vuelca el estado del agregado sobre una entidad destino (nueva o existente),
     * preservando la PK interna si la entidad ya tenía una.
     */
    public void copiarADestino(TipoInstitucion tipo, TipoInstitucionEntity destino) {
        destino.setAltKey(tipo.altKey());
        destino.setNombre(tipo.nombre());
        destino.setAmbito(tipo.ambito().name());
        destino.setDescripcion(tipo.descripcion());
        destino.setVigente(tipo.vigente());
        destino.setCreadoEn(MarcasTiempo.aLocal(tipo.creadoEn()));
        destino.setActualizadoEn(MarcasTiempo.aLocal(tipo.actualizadoEn()));
    }
}
