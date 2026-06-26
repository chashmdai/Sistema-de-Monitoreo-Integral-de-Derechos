package cl.smid.personas.soporte;

import cl.smid.personas.dominio.modelo.ContextoTerritorial;
import cl.smid.personas.dominio.modelo.Pagina;
import cl.smid.personas.dominio.modelo.Persona;
import cl.smid.personas.dominio.puerto.salida.PersonaRepositorio;
import cl.smid.personas.dominio.servicio.EvaluadorAlcance;
import cl.smid.personas.dominio.servicio.NormalizadorTexto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementación en memoria del puerto {@link PersonaRepositorio}, para pruebas unitarias del
 * dominio sin contenedores ni base de datos. Reproduce fielmente la semántica relevante: la
 * obtención por {@code altKey} es incondicional (el filtro de alcance lo aplica el servicio),
 * mientras que las búsquedas por listado filtran por alcance; la deduplicación es
 * cross-territorial.
 *
 * <p>No pretende replicar el SQL exacto del adaptador JPA, sino su comportamiento observable,
 * que es lo que las pruebas de dominio verifican.</p>
 */
public class PersonaRepositorioEnMemoria implements PersonaRepositorio {

    /** Largo del prefijo de apellido usado para el "blocking", igual al del buscador. */
    private static final int LARGO_PREFIJO_APELLIDO = 3;

    private final Map<Long, Persona> almacen = new LinkedHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(0);

    @Override
    public Persona guardar(Persona persona) {
        Long id = persona.id();
        if (id == null) {
            id = secuencia.incrementAndGet();
        }
        Persona almacenada = persona.copia().id(id).construir();
        almacen.put(id, almacenada);
        return almacenada;
    }

    @Override
    public Optional<Persona> buscarPorAltKey(String altKey) {
        return almacen.values().stream()
                .filter(p -> p.altKey() != null && p.altKey().equals(altKey))
                .findFirst();
    }

    @Override
    public Optional<Persona> buscarVigentePorRutGlobal(String rutCanonico) {
        if (rutCanonico == null) {
            return Optional.empty();
        }
        return almacen.values().stream()
                .filter(Persona::vigente)
                .filter(p -> rutCanonico.equals(p.rut()))
                .findFirst();
    }

    @Override
    public Optional<Persona> buscarVigentePorRutEnAlcance(String rutCanonico, ContextoTerritorial ctx) {
        return buscarVigentePorRutGlobal(rutCanonico)
                .filter(p -> EvaluadorAlcance.dentroDeAlcance(p.idSede(), p.idUnidad(), ctx));
    }

    @Override
    public Pagina<Persona> buscarPorNombreEnAlcance(String termino, ContextoTerritorial ctx,
                                                    int pagina, int tamano) {
        String t = NormalizadorTexto.normalizar(termino);

        List<Persona> coincidencias = almacen.values().stream()
                .filter(Persona::vigente)
                .filter(p -> EvaluadorAlcance.dentroDeAlcance(p.idSede(), p.idUnidad(), ctx))
                .filter(p -> textoBusqueda(p).contains(t))
                .toList();

        long total = coincidencias.size();
        int desde = Math.max(pagina, 0) * tamano;
        int hasta = Math.min(desde + tamano, coincidencias.size());
        List<Persona> ventana = (desde >= coincidencias.size())
                ? List.of()
                : new ArrayList<>(coincidencias.subList(desde, hasta));

        return new Pagina<>(ventana, pagina, tamano, total);
    }

    @Override
    public List<Persona> candidatosParaDedup(String prefijoApellido, LocalDate fechaNacimiento) {
        boolean hayPrefijo = prefijoApellido != null && !prefijoApellido.isBlank();
        if (!hayPrefijo && fechaNacimiento == null) {
            return List.of();
        }
        List<Persona> candidatos = new ArrayList<>();
        for (Persona p : almacen.values()) {
            if (!p.vigente()) {
                continue;
            }
            boolean coincideFecha = fechaNacimiento != null && fechaNacimiento.equals(p.fechaNacimiento());
            boolean coincidePrefijo = hayPrefijo && prefijoApellidoDe(p).startsWith(prefijoApellido);
            if (coincideFecha || coincidePrefijo) {
                candidatos.add(p);
            }
        }
        return candidatos;
    }

    // ----------------------------- Apoyo -----------------------------

    /** Texto sobre el que se hace la búsqueda parcial por nombre, ya normalizado. */
    private static String textoBusqueda(Persona p) {
        return NormalizadorTexto.normalizar(
                (p.nombres() == null ? "" : p.nombres()) + " "
                        + (p.apellidoPaterno() == null ? "" : p.apellidoPaterno()) + " "
                        + (p.apellidoMaterno() == null ? "" : p.apellidoMaterno()) + " "
                        + (p.razonSocial() == null ? "" : p.razonSocial()));
    }

    /** Prefijo normalizado del apellido paterno del registro (mismo criterio que el buscador). */
    private static String prefijoApellidoDe(Persona p) {
        String norm = NormalizadorTexto.normalizarAlfanumerico(p.apellidoPaterno());
        if (norm.isEmpty()) {
            return "";
        }
        return norm.substring(0, Math.min(LARGO_PREFIJO_APELLIDO, norm.length()));
    }
}
