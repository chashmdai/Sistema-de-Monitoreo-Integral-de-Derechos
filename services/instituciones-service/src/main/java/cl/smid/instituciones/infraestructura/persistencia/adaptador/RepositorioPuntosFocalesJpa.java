package cl.smid.instituciones.infraestructura.persistencia.adaptador;

import cl.smid.instituciones.dominio.excepcion.RecursoNoEncontradoException;
import cl.smid.instituciones.dominio.modelo.PuntoFocal;
import cl.smid.instituciones.dominio.puerto.salida.RepositorioPuntosFocales;
import cl.smid.instituciones.infraestructura.persistencia.entidad.InstitucionEntity;
import cl.smid.instituciones.infraestructura.persistencia.entidad.PuntoFocalEntity;
import cl.smid.instituciones.infraestructura.persistencia.jpa.InstitucionJpa;
import cl.smid.instituciones.infraestructura.persistencia.jpa.PuntoFocalJpa;
import cl.smid.instituciones.infraestructura.persistencia.mapeador.MapeadorPuntoFocal;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia (JPA) del puerto {@link RepositorioPuntosFocales}. Resuelve
 * la traducción {@code institucionAlt <-> institucion_id} y delega la operación masiva
 * de desmarcado de principales en la consulta {@code @Modifying} del repositorio.
 */
@Repository
public class RepositorioPuntosFocalesJpa implements RepositorioPuntosFocales {

    private final PuntoFocalJpa jpa;
    private final InstitucionJpa institucionJpa;
    private final MapeadorPuntoFocal mapeador;

    public RepositorioPuntosFocalesJpa(PuntoFocalJpa jpa, InstitucionJpa institucionJpa,
                                       MapeadorPuntoFocal mapeador) {
        this.jpa = jpa;
        this.institucionJpa = institucionJpa;
        this.mapeador = mapeador;
    }

    @Override
    public PuntoFocal guardar(PuntoFocal puntoFocal) {
        Long institucionId = idInstitucion(puntoFocal.institucionAlt());
        PuntoFocalEntity entidad = jpa.findByAltKey(puntoFocal.altKey())
                .orElseGet(PuntoFocalEntity::new);
        mapeador.copiarADestino(puntoFocal, institucionId, entidad);
        PuntoFocalEntity guardado = jpa.save(entidad);
        return mapeador.aDominio(guardado, puntoFocal.institucionAlt());
    }

    @Override
    public Optional<PuntoFocal> buscarPorAlt(String altKey) {
        return jpa.findByAltKey(altKey)
                .map(entidad -> mapeador.aDominio(entidad, altKeyDeInstitucion(entidad.getInstitucionId())));
    }

    @Override
    public List<PuntoFocal> listarPorInstitucion(String institucionAlt) {
        Optional<InstitucionEntity> institucion = institucionJpa.findByAltKey(institucionAlt);
        if (institucion.isEmpty()) {
            return List.of();
        }
        return jpa.findByInstitucionIdOrderByPrincipalDescIdAsc(institucion.get().getId()).stream()
                .map(entidad -> mapeador.aDominio(entidad, institucionAlt))
                .toList();
    }

    @Override
    public void desmarcarOtrosPrincipales(String institucionAlt, String altKeyActual) {
        institucionJpa.findByAltKey(institucionAlt).ifPresent(institucion ->
                jpa.desmarcarOtrosPrincipales(institucion.getId(), altKeyActual));
    }

    private Long idInstitucion(String institucionAlt) {
        return institucionJpa.findByAltKey(institucionAlt)
                .map(InstitucionEntity::getId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una institución con alt_key '" + institucionAlt + "'."));
    }

    private String altKeyDeInstitucion(Long institucionId) {
        return institucionJpa.findById(institucionId)
                .map(InstitucionEntity::getAltKey)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "La institución asociada al punto focal no existe."));
    }
}
