package cl.smid.smidservice.service;

import cl.smid.smidservice.entity.TramiteLegislativo;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class LegislativoService {

    private static final BigDecimal PESO_IRN = new BigDecimal("0.70");
    private static final BigDecimal PESO_IRRI = new BigDecimal("0.30");

    public BigDecimal calcularIRN(TramiteLegislativo tramite) {
        double suma = tramite.getScoreInteresSup() +
                      tramite.getScoreParticipacion() +
                      tramite.getScoreAutonomia() +
                      tramite.getScoreInterseccion() +
                      tramite.getScoreRolGarante();

        return BigDecimal.valueOf(suma / 5.0).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularIRRI(TramiteLegislativo tramite) {
        double suma = tramite.getScoreProporcion() +
                      tramite.getScoreOperatividad();

        return BigDecimal.valueOf(suma / 2.0).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularIndiceCompuesto(BigDecimal irn, BigDecimal irri) {
        return irn.multiply(PESO_IRN)
                  .add(irri.multiply(PESO_IRRI))
                  .setScale(2, RoundingMode.HALF_UP);
    }
}
