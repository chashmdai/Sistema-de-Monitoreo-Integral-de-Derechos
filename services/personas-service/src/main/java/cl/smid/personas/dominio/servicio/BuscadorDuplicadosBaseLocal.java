package cl.smid.personas.dominio.servicio;

import cl.smid.personas.dominio.modelo.CoincidenciaExacta;
import cl.smid.personas.dominio.modelo.CoincidenciaProbable;
import cl.smid.personas.dominio.modelo.CriterioDuplicados;
import cl.smid.personas.dominio.modelo.Persona;
import cl.smid.personas.dominio.modelo.ResultadoDuplicados;
import cl.smid.personas.dominio.modelo.Rut;
import cl.smid.personas.dominio.puerto.salida.BuscadorDuplicados;
import cl.smid.personas.dominio.puerto.salida.PersonaRepositorio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador de dominio (puro) del puerto {@link BuscadorDuplicados} que coteja exclusivamente
 * la <b>base local SMID</b> (Núcleo 5.5). No depende de framework: recibe el repositorio por
 * constructor y usa utilidades puras de normalización y similitud.
 *
 * <p>Estrategia:</p>
 * <ol>
 *   <li><b>Exacta</b>: si el criterio trae RUT y éste es válido por módulo 11, se busca una
 *       persona vigente con ese RUT canónico en toda la base. Si existe, se reporta con motivo
 *       {@code "RUT"}.</li>
 *   <li><b>Probable (difusa)</b>: se recuperan candidatos por "blocking" (mismo apellido por
 *       prefijo o misma fecha de nacimiento) y se puntúa cada uno combinando la similitud
 *       Jaro-Winkler del nombre normalizado con la coincidencia de fecha. Se filtran por umbral,
 *       se ordenan por score descendente y se acotan.</li>
 * </ol>
 *
 * <p>Es cross-territorial por diseño: la deduplicación debe ver todas las sedes. Sólo emite datos
 * mínimos (alt_key, nombre legible, fecha, score), nunca el registro completo (G7).</p>
 *
 * <p><b>Mejoras futuras documentadas:</b> el "blocking" actual (prefijo de apellido / fecha)
 * puede enriquecerse con n-gramas o codificación fonética, y puede añadirse un segundo adaptador
 * que consulte a {@code siger-service}. Ambas evoluciones caben sin tocar el dominio gracias al
 * puerto.</p>
 */
public final class BuscadorDuplicadosBaseLocal implements BuscadorDuplicados {

    /** Peso de la similitud de nombre en el score combinado. */
    private static final double PESO_NOMBRE = 0.82;

    /** Peso de la coincidencia de fecha de nacimiento en el score combinado. */
    private static final double PESO_FECHA = 0.18;

    /** Umbral mínimo de score para considerar una coincidencia "probable". */
    private static final double UMBRAL_PROBABLE = 0.78;

    /** Máximo de coincidencias probables devueltas. */
    private static final int MAX_PROBABLES = 10;

    /** Largo del prefijo de apellido usado para el "blocking". */
    private static final int LARGO_PREFIJO_APELLIDO = 3;

    private final PersonaRepositorio repositorio;

    public BuscadorDuplicadosBaseLocal(PersonaRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public ResultadoDuplicados buscar(CriterioDuplicados criterio) {
        if (criterio == null) {
            return ResultadoDuplicados.vacio();
        }

        Optional<CoincidenciaExacta> exacta = buscarExactaPorRut(criterio);
        List<CoincidenciaProbable> probables = buscarProbables(criterio);

        return new ResultadoDuplicados(exacta, probables);
    }

    /**
     * Coincidencia exacta por RUT. Sólo se intenta si el RUT viene informado; si es inválido se
     * ignora silenciosamente para la prevalidación (la validación dura se hará en el alta).
     */
    private Optional<CoincidenciaExacta> buscarExactaPorRut(CriterioDuplicados criterio) {
        if (criterio.rut() == null || criterio.rut().isBlank()) {
            return Optional.empty();
        }
        Rut rut;
        try {
            rut = Rut.normalizarYValidar(criterio.rut());
        } catch (RuntimeException e) {
            // RUT mal formado en la prevalidación: no hay match exacto que reportar.
            return Optional.empty();
        }
        return repositorio.buscarVigentePorRutGlobal(rut.canonico())
                .map(p -> new CoincidenciaExacta(p.altKey(), "RUT"));
    }

    /**
     * Coincidencias probables por similitud de nombre y fecha. Si no hay datos suficientes
     * (ni nombre/apellido ni fecha) devuelve lista vacía.
     */
    private List<CoincidenciaProbable> buscarProbables(CriterioDuplicados criterio) {
        String prefijo = prefijoApellido(criterio.apellidoPaterno());
        LocalDate fecha = criterio.fechaNacimiento();

        boolean sinNombre = (prefijo == null || prefijo.isEmpty());
        if (sinNombre && fecha == null) {
            return List.of();
        }

        // Cadena de comparación del criterio: sólo campos informados, ya normalizados.
        String nombreQuery = comparableNombre(
                criterio.apellidoPaterno(), criterio.apellidoMaterno(), criterio.nombres());

        List<Persona> candidatos = repositorio.candidatosParaDedup(prefijo, fecha);
        List<CoincidenciaProbable> resultado = new ArrayList<>();

        for (Persona cand : candidatos) {
            String nombreCand = comparableNombreSegunCriterio(criterio, cand);

            double sim = CalculadorSimilitud.similitud(nombreQuery, nombreCand);
            double scoreFecha = puntajeFecha(fecha, cand.fechaNacimiento());
            double score = PESO_NOMBRE * sim + PESO_FECHA * scoreFecha;

            if (score >= UMBRAL_PROBABLE) {
                resultado.add(new CoincidenciaProbable(
                        cand.altKey(), cand.nombreLegible(), cand.fechaNacimiento(), redondear(score)));
            }
        }

        resultado.sort(Comparator.comparingDouble(CoincidenciaProbable::score).reversed());
        if (resultado.size() > MAX_PROBABLES) {
            return new ArrayList<>(resultado.subList(0, MAX_PROBABLES));
        }
        return resultado;
    }

    /**
     * Puntaje de coincidencia de fecha de nacimiento: 1.0 si ambas presentes e iguales, 0.0 si
     * ambas presentes y distintas, 0.5 si falta alguna (información incompleta, no penaliza ni
     * premia con fuerza).
     */
    private static double puntajeFecha(LocalDate a, LocalDate b) {
        if (a == null || b == null) {
            return 0.5;
        }
        return a.equals(b) ? 1.0 : 0.0;
    }

    /** Construye la cadena comparable del nombre (apellidos + nombres) ya normalizada. */
    private static String comparableNombre(String apPaterno, String apMaterno, String nombres) {
        return NormalizadorTexto.normalizar(
                (apPaterno == null ? "" : apPaterno) + " "
                        + (apMaterno == null ? "" : apMaterno) + " "
                        + (nombres == null ? "" : nombres));
    }

    /** Construye la cadena comparable del candidato usando sólo los campos presentes en el criterio. */
    private static String comparableNombreSegunCriterio(CriterioDuplicados criterio, Persona candidato) {
        return comparableNombre(
                criterio.apellidoPaterno() == null ? null : candidato.apellidoPaterno(),
                criterio.apellidoMaterno() == null ? null : candidato.apellidoMaterno(),
                criterio.nombres() == null ? null : candidato.nombres());
    }

    /** Prefijo normalizado del apellido paterno para el "blocking", o cadena vacía. */
    private static String prefijoApellido(String apellidoPaterno) {
        String norm = NormalizadorTexto.normalizarAlfanumerico(apellidoPaterno);
        if (norm.isEmpty()) {
            return "";
        }
        return norm.substring(0, Math.min(LARGO_PREFIJO_APELLIDO, norm.length()));
    }

    /** Redondeo a 4 decimales para un score estable y legible. */
    private static double redondear(double v) {
        return Math.round(v * 10000.0) / 10000.0;
    }
}
