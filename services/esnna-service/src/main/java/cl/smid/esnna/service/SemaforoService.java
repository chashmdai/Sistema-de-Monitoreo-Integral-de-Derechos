package cl.smid.esnna.service;

import cl.smid.esnna.domain.Semaforo;
import cl.smid.esnna.dto.CriterioEvaluadoDTO;
import cl.smid.esnna.dto.CriteriosSemaforoDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * FEAT semáforo-en-backend: la IA entrega evidencia por criterio; acá se aplica
 * la regla copulativa de forma determinista y se arma la justificación legible.
 * El color deja de ser una caja negra del modelo.
 */
@Service
public class SemaforoService {

    public Semaforo computar(CriteriosSemaforoDTO c) {
        if (c == null) return Semaforo.VERDE;
        return Semaforo.evaluar(c.cumplidos(), c.exclusionAplicable(), c.improcedenciaPmaNad());
    }

    public String justificar(CriteriosSemaforoDTO c) {
        if (c == null) return "Sin criterios evaluados; se asigna VERDE por información insuficiente.";

        if (c.exclusionAplicable()) {
            return "VERDE por exclusión del protocolo: "
                    + (c.cualExclusion() != null ? c.cualExclusion() : "no especificada") + ".";
        }
        if (c.improcedenciaPmaNad()) {
            return "VERDE por improcedencia institucional (PMA/NAD): "
                    + (c.detalleImprocedencia() != null ? c.detalleImprocedencia() : "PMA/NAD ya interviene") + ".";
        }

        List<String> verificados = new ArrayList<>();
        agregar(verificados, "C1 Pluralidad de NNA", c.c1PluralidadNna());
        agregar(verificados, "C2 Interseccionalidad", c.c2Interseccionalidad());
        agregar(verificados, "C3 Agente cualificado", c.c3AgenteCualificado());
        agregar(verificados, "C4 Pluralidad de victimarios/red", c.c4PluralidadVictimarios());
        agregar(verificados, "C5 Causa basal estructural", c.c5CausaBasal());

        int n = c.cumplidos();
        Semaforo s = computar(c);
        String cabecera = switch (s) {
            case ROJO -> "ROJO: " + n + " criterios verificados (>=3 copulativos).";
            case AMARILLO -> "AMARILLO: 2 criterios verificados; caso límite, requiere revisión humana.";
            case VERDE -> "VERDE: " + n + " criterio(s) verificado(s) (<=1).";
        };
        if (verificados.isEmpty()) {
            return cabecera;
        }
        return cabecera + " Detalle: " + String.join("; ", verificados) + ".";
    }

    private void agregar(List<String> acc, String etiqueta, CriterioEvaluadoDTO crit) {
        if (crit != null && crit.cumple()) {
            String ev = (crit.evidencia() != null && !crit.evidencia().isBlank())
                    ? " (" + crit.evidencia() + ")" : "";
            acc.add(etiqueta + ev);
        }
    }
}
