package cl.smid.antecedentes.dominio;

import cl.smid.antecedentes.dominio.modelo.Alcance;
import cl.smid.antecedentes.dominio.modelo.Calificacion;
import cl.smid.antecedentes.dominio.modelo.ContextoSesion;
import cl.smid.antecedentes.dominio.modelo.FichaAntecedente;
import cl.smid.antecedentes.dominio.modelo.PercepcionHallazgo;
import cl.smid.antecedentes.dominio.servicio.EvaluadorAlcance;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EvaluadorAlcanceTest {

    private final EvaluadorAlcance evaluador = new EvaluadorAlcance();

    private FichaAntecedente fichaDe(String unidad, String sede) {
        return FichaAntecedente.crear("ficha-1", "FA-1/2027", unidad, sede, "prof-1", null,
                "proc-1", null, "cat-1", List.of(), List.of(), "desc", "relato",
                Calificacion.BUENA_PRACTICA, Set.of(), PercepcionHallazgo.NO_ES_HALLAZGO, null, Instant.EPOCH);
    }

    private ContextoSesion contexto(Alcance alcance, String sede, String unidad) {
        return new ContextoSesion("u", "Nombre", Set.of(), sede, unidad, alcance);
    }

    @Test
    void nacionalVeTodo() {
        assertTrue(evaluador.puedeVer(contexto(Alcance.NACIONAL, "sX", "uX"), fichaDe("uA", "sA")));
    }

    @Test
    void sedeVeSoloSuSede() {
        FichaAntecedente ficha = fichaDe("uA", "sA");
        assertTrue(evaluador.puedeVer(contexto(Alcance.SEDE, "sA", "uX"), ficha));
        assertFalse(evaluador.puedeVer(contexto(Alcance.SEDE, "sB", "uX"), ficha));
    }

    @Test
    void unidadVeSoloSuUnidad() {
        FichaAntecedente ficha = fichaDe("uA", "sA");
        assertTrue(evaluador.puedeVer(contexto(Alcance.UNIDAD, "sA", "uA"), ficha));
        assertFalse(evaluador.puedeVer(contexto(Alcance.UNIDAD, "sA", "uB"), ficha));
    }

    @Test
    void edicionExigeMismaUnidadAunSiendoNacional() {
        FichaAntecedente ficha = fichaDe("uA", "sA");
        assertTrue(evaluador.perteneceMismaUnidad(contexto(Alcance.NACIONAL, "sA", "uA"), ficha));
        assertFalse(evaluador.perteneceMismaUnidad(contexto(Alcance.NACIONAL, "sZ", "uB"), ficha));
    }
}
