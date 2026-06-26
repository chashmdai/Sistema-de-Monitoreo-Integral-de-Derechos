package cl.smid.requerimientos.infraestructura.persistencia.adaptador;

import cl.smid.requerimientos.dominio.modelo.SerieFolio;
import cl.smid.requerimientos.dominio.puerto.salida.CorrelativoFolioRepositorio;
import cl.smid.requerimientos.infraestructura.persistencia.repositorio.CorrelativoFolioJpaRepository;
import org.springframework.stereotype.Component;

/**
 * Adaptador de salida que implementa {@link CorrelativoFolioRepositorio}. Reserva el siguiente
 * correlativo de forma atómica con un UPSERT de MySQL (bloqueo exclusivo de fila hasta el commit) y
 * lee de inmediato el valor reservado. Se ejecuta dentro de la transacción del controlador, por lo
 * que la reserva queda atada al commit del alta del requerimiento.
 */
@Component
public class CorrelativoFolioRepositorioJpa implements CorrelativoFolioRepositorio {

    private final CorrelativoFolioJpaRepository jpaRepository;

    public CorrelativoFolioRepositorioJpa(CorrelativoFolioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public long siguienteCorrelativo(String idSedeAlt, int anio, SerieFolio serie) {
        String serieTexto = serie.name();
        // UPSERT atómico: inserta en 1 o incrementa en 1 bajo bloqueo de fila.
        jpaRepository.incrementarOInsertar(idSedeAlt, anio, serieTexto);
        // La transacción ve su propia escritura: este valor es el correlativo reservado.
        Long valor = jpaRepository.leerValor(idSedeAlt, anio, serieTexto);
        if (valor == null) {
            throw new IllegalStateException(
                    "No se pudo reservar el correlativo de folio para sede " + idSedeAlt
                            + ", año " + anio + ", serie " + serieTexto + ".");
        }
        return valor;
    }
}
