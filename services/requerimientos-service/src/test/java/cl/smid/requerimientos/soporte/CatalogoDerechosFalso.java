package cl.smid.requerimientos.soporte;

import cl.smid.requerimientos.dominio.puerto.salida.CatalogoDerechos;

import java.util.HashSet;
import java.util.Set;

/** Catálogo de derechos falso: conjuntos de derechos y de pares derecho|causa válidos. */
public class CatalogoDerechosFalso implements CatalogoDerechos {

    private final Set<String> derechos = new HashSet<>();
    private final Set<String> causas = new HashSet<>();

    public CatalogoDerechosFalso registrarDerecho(String idDerechoAlt) {
        derechos.add(idDerechoAlt);
        return this;
    }

    public CatalogoDerechosFalso registrarCausa(String idDerechoAlt, String idCausaAlt) {
        derechos.add(idDerechoAlt);
        causas.add(idDerechoAlt + "|" + idCausaAlt);
        return this;
    }

    @Override
    public boolean existeDerecho(String idDerechoAlt) {
        return derechos.contains(idDerechoAlt);
    }

    @Override
    public boolean existeCausa(String idDerechoAlt, String idCausaAlt) {
        return causas.contains(idDerechoAlt + "|" + idCausaAlt);
    }
}
