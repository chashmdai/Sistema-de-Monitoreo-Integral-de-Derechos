package cl.smid.antecedentes.infraestructura.persistencia;

/**
 * Componente de infraestructura para cifrar/descifrar campos reservados en reposo. El dominio
 * <strong>no conoce</strong> el cifrado: el mapeador cifra al escribir y descifra al leer.
 * Tiene dos implementaciones conmutables por {@code smid.cifrado.relato}:
 * {@code aes-gcm} (AES-256-GCM) y {@code none} (passthrough, solo desarrollo).
 */
public interface CifradorCampos {

    /**
     * Cifra un texto plano y devuelve su representacion persistible.
     *
     * @param textoPlano texto en claro (puede ser nulo)
     * @return texto cifrado/codificado (nulo si la entrada es nula)
     */
    String cifrar(String textoPlano);

    /**
     * Descifra una representacion persistida y devuelve el texto plano.
     *
     * @param almacenado texto cifrado/codificado (puede ser nulo)
     * @return texto en claro (nulo si la entrada es nula)
     */
    String descifrar(String almacenado);
}
