package cl.smid.catalogo.infraestructura.persistencia.adaptador;

import cl.smid.catalogo.dominio.modelo.Derecho;
import cl.smid.catalogo.dominio.puerto.salida.DerechoRepositorio;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Adaptador de salida que implementa el puerto {@link DerechoRepositorio} del dominio sobre
 * Spring Data JPA. Traduce entre modelo de dominio y entidad mediante {@link MapeadorPersistencia}
 * y delega las consultas (incluidas las CTE recursivas) en {@link SpringDataDerechoRepository}.
 */
@Repository
public class DerechoRepositorioJpa implements DerechoRepositorio {

    private final SpringDataDerechoRepository jpa;
    private final MapeadorPersistencia mapeador;

    public DerechoRepositorioJpa(SpringDataDerechoRepository jpa, MapeadorPersistencia mapeador) {
        this.jpa = jpa;
        this.mapeador = mapeador;
    }

    @Override
    public List<Derecho> cargarArbolVigente() {
        return jpa.cargarArbolVigente().stream().map(mapeador::aDominio).toList();
    }

    @Override
    public List<Derecho> cargarTodosVigentes() {
        return jpa.findByVigenteTrueOrderByNivelAscOrdenAscCodigoAsc()
                .stream().map(mapeador::aDominio).toList();
    }

    @Override
    public List<Derecho> descendientesVigentes(Long idRaiz) {
        return jpa.descendientesVigentes(idRaiz).stream().map(mapeador::aDominio).toList();
    }

    @Override
    public List<Long> idsDescendientes(Long idRaiz) {
        return jpa.idsDescendientes(idRaiz);
    }

    @Override
    public List<Derecho> hijosDirectosVigentes(Long idPadre) {
        return jpa.findByIdPadreAndVigenteTrueOrderByOrdenAscCodigoAsc(idPadre)
                .stream().map(mapeador::aDominio).toList();
    }

    @Override
    public Optional<Derecho> buscarPorAltKey(String altKey) {
        return jpa.findByAltKey(altKey).map(mapeador::aDominio);
    }

    @Override
    public Optional<Derecho> buscarPorCodigo(String codigo) {
        return jpa.findByCodigo(codigo).map(mapeador::aDominio);
    }

    @Override
    public boolean existeCodigo(String codigo) {
        return jpa.existsByCodigo(codigo);
    }

    @Override
    public List<Derecho> buscarPorTexto(String texto) {
        // Comodines para una coincidencia parcial; la colación ai_ci aporta la insensibilidad.
        String patron = "%" + texto + "%";
        return jpa.buscarPorTexto(patron).stream().map(mapeador::aDominio).toList();
    }

    @Override
    public Derecho guardarNuevo(Derecho derecho) {
        var entidad = Objects.requireNonNull(mapeador.aEntidad(derecho));
        var guardada = jpa.save(entidad);
        // Propaga el id generado al modelo de dominio para conservar la identidad.
        derecho.asignarId(guardada.getId());
        return derecho;
    }

    @Override
    public void actualizar(Derecho derecho) {
        // save() sobre una entidad con id presente actúa como actualización (merge).
        jpa.save(Objects.requireNonNull(mapeador.aEntidad(derecho)));
    }

    @Override
    public void actualizarTodos(List<Derecho> derechos) {
        jpa.saveAll(Objects.requireNonNull(mapeador.aEntidades(derechos)));
    }
}
