package cl.smid.antecedentes.infraestructura.persistencia;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Passthrough sin cifrado ({@code smid.cifrado.relato=none}). <strong>Solo desarrollo</strong>
 * con datos sinteticos: el relato se persiste en claro. Nunca debe usarse en produccion.
 */
@Component
@ConditionalOnProperty(prefix = "smid.cifrado", name = "relato", havingValue = "none")
public class CifradorPassthrough implements CifradorCampos {

    @Override
    public String cifrar(String textoPlano) {
        return textoPlano;
    }

    @Override
    public String descifrar(String almacenado) {
        return almacenado;
    }
}
