package cl.smid.antecedentes.infraestructura;

import cl.smid.antecedentes.infraestructura.persistencia.CifradorAesGcm;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CifradorAesGcmTest {

    private CifradorAesGcm cifradorConClaveValida() {
        byte[] clave = new byte[32];
        for (int i = 0; i < clave.length; i++) {
            clave[i] = (byte) i;
        }
        MockEnvironment env = new MockEnvironment()
                .withProperty("ANTECEDENTES_CIFRADO_CLAVE", Base64.getEncoder().encodeToString(clave));
        return new CifradorAesGcm(env);
    }

    @Test
    void idaYVuelta() {
        CifradorAesGcm cifrador = cifradorConClaveValida();
        String original = "Relato reservado con tildes: nino, accion, mision.";
        String cifrado = cifrador.cifrar(original);
        assertNotEquals(original, cifrado);
        assertEquals(original, cifrador.descifrar(cifrado));
    }

    @Test
    void nulosSeManejan() {
        CifradorAesGcm cifrador = cifradorConClaveValida();
        assertNull(cifrador.cifrar(null));
        assertNull(cifrador.descifrar(null));
    }

    @Test
    void ivAleatorioProduceCifradosDistintos() {
        CifradorAesGcm cifrador = cifradorConClaveValida();
        String original = "mismo texto";
        assertNotEquals(cifrador.cifrar(original), cifrador.cifrar(original));
    }

    @Test
    void manipulacionDelCifradoFalla() {
        CifradorAesGcm cifrador = cifradorConClaveValida();
        String cifrado = cifrador.cifrar("contenido integro");
        byte[] datos = Base64.getDecoder().decode(cifrado);
        datos[datos.length - 1] ^= 0x01; // altera el tag de autenticacion
        String manipulado = Base64.getEncoder().encodeToString(datos);
        assertThrows(IllegalStateException.class, () -> cifrador.descifrar(manipulado));
    }

    @Test
    void claveDeLongitudInvalidaFallaAlConstruir() {
        MockEnvironment env = new MockEnvironment()
                .withProperty("ANTECEDENTES_CIFRADO_CLAVE", Base64.getEncoder().encodeToString(new byte[16]));
        assertThrows(IllegalStateException.class, () -> new CifradorAesGcm(env));
    }
}
