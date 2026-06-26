package cl.smid.catalogo.api;

import cl.smid.catalogo.api.dto.CausaDTO;
import cl.smid.catalogo.api.dto.DerechoArbolDTO;
import cl.smid.catalogo.api.dto.DerechoDetalleDTO;
import cl.smid.catalogo.api.dto.DerechoPlanoDTO;
import cl.smid.catalogo.dominio.modelo.Causa;
import cl.smid.catalogo.dominio.modelo.Derecho;
import cl.smid.catalogo.dominio.modelo.NodoArbol;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Proyecta los modelos de dominio a DTOs de la API. <b>Esta clase es la frontera</b> que
 * garantiza el cierre de IDOR (Núcleo 2.2): las PK/FK internas (BIGINT) se usan, como mucho,
 * para resolver relaciones aquí dentro (p. ej. el alt_key del padre en la vista plana), pero
 * nunca se serializan en la respuesta.
 */
@Component
public class MapeadorRespuesta {

    // ----------------------------- Árbol -----------------------------

    public List<DerechoArbolDTO> aArbol(List<NodoArbol> nodos) {
        List<DerechoArbolDTO> resultado = new ArrayList<>(nodos.size());
        for (NodoArbol nodo : nodos) {
            resultado.add(aArbol(nodo));
        }
        return resultado;
    }

    /** Convierte recursivamente un nodo y todo su subárbol. */
    public DerechoArbolDTO aArbol(NodoArbol nodo) {
        Derecho d = nodo.getDerecho();
        List<DerechoArbolDTO> hijos = new ArrayList<>(nodo.getHijos().size());
        for (NodoArbol hijo : nodo.getHijos()) {
            hijos.add(aArbol(hijo));
        }
        return new DerechoArbolDTO(d.getAltKey(), d.getCodigo(), d.getNombre(), d.getNivel(), hijos);
    }

    // ----------------------------- Detalle -----------------------------

    public DerechoDetalleDTO aDetalle(NodoArbol nodo) {
        Derecho d = nodo.getDerecho();
        List<DerechoArbolDTO> hijos = new ArrayList<>(nodo.getHijos().size());
        for (NodoArbol hijo : nodo.getHijos()) {
            hijos.add(aArbol(hijo));
        }
        return new DerechoDetalleDTO(
                d.getAltKey(), d.getCodigo(), d.getNombre(), d.getNivel(),
                d.getDescripcion(), d.estaVigente(), hijos);
    }

    // ----------------------------- Plano -----------------------------

    /**
     * Proyecta la vista plana resolviendo el alt_key del padre de cada nodo a partir de un
     * índice {@code id interno → alt_key} construido con la propia lista. El id interno solo
     * se usa para esa resolución y no aparece en la salida; si el padre no está en la lista
     * (p. ej. quedó fuera del conjunto vigente), el alt_key del padre se deja nulo.
     */
    public List<DerechoPlanoDTO> aPlano(List<Derecho> derechos) {
        Map<Long, String> altKeyPorId = new HashMap<>(derechos.size() * 2);
        for (Derecho d : derechos) {
            altKeyPorId.put(d.getId(), d.getAltKey());
        }
        List<DerechoPlanoDTO> resultado = new ArrayList<>(derechos.size());
        for (Derecho d : derechos) {
            String altKeyPadre = (d.getIdPadre() == null) ? null : altKeyPorId.get(d.getIdPadre());
            resultado.add(new DerechoPlanoDTO(
                    d.getAltKey(), d.getCodigo(), d.getNombre(), d.getNivel(), altKeyPadre));
        }
        return resultado;
    }

    // ----------------------------- Causa -----------------------------

    public CausaDTO aCausa(Causa causa) {
        return new CausaDTO(causa.getAltKey(), causa.getCodigo(), causa.getNombre(), causa.estaVigente());
    }

    public List<CausaDTO> aCausas(List<Causa> causas) {
        List<CausaDTO> resultado = new ArrayList<>(causas.size());
        for (Causa causa : causas) {
            resultado.add(aCausa(causa));
        }
        return resultado;
    }
}
