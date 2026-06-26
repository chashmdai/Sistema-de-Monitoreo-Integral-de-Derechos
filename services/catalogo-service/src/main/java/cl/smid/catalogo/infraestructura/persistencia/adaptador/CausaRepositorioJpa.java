package cl.smid.catalogo.infraestructura.persistencia.adaptador;

import cl.smid.catalogo.dominio.modelo.Causa;
import cl.smid.catalogo.dominio.puerto.salida.CausaRepositorio;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * Adaptador de salida que implementa el puerto {@link CausaRepositorio} sobre Spring Data JPA.
 */
@Repository
public class CausaRepositorioJpa implements CausaRepositorio {

    private final SpringDataCausaRepository jpa;
    private final MapeadorPersistencia mapeador;

    public CausaRepositorioJpa(SpringDataCausaRepository jpa, MapeadorPersistencia mapeador) {
        this.jpa = jpa;
        this.mapeador = mapeador;
    }

    @Override
    public List<Causa> causasVigentesDe(Long idDerecho) {
        return jpa.findByIdDerechoAndVigenteTrueOrderByCodigoAsc(idDerecho)
                .stream().map(mapeador::aDominio).toList();
    }

    @Override
    public boolean existeCodigoEnDerecho(Long idDerecho, String codigo) {
        return jpa.existsByIdDerechoAndCodigo(idDerecho, codigo);
    }

    @Override
    public Causa guardarNueva(Causa causa) {
        var entidad = Objects.requireNonNull(mapeador.aEntidad(causa));
        var guardada = jpa.save(entidad);
        causa.asignarId(guardada.getId());
        return causa;
    }
}
