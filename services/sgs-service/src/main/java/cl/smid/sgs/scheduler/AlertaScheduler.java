package cl.smid.sgs.scheduler;

import cl.smid.sgs.entity.AlertaSeguimiento;
import cl.smid.sgs.entity.Oficio;
import cl.smid.sgs.entity.Recomendacion;
import cl.smid.sgs.entity.Seguimiento;
import cl.smid.sgs.enums.EstadoGestion;
import cl.smid.sgs.enums.FaseSeguimiento;
import cl.smid.sgs.enums.TipoAlerta;
import cl.smid.sgs.repository.AlertaRepository;
import cl.smid.sgs.repository.RecomendacionRepository;
import cl.smid.sgs.service.TelegramNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Barrido diario (decisión #6): computa los dos relojes (decisión #4), registra alertas con dedup
 * y notifica por Telegram. GV endurece el umbral (avisa antes).
 */
@Component
public class AlertaScheduler {

    private static final Logger log = LoggerFactory.getLogger(AlertaScheduler.class);
    private static final Set<EstadoGestion> TERMINALES =
            EnumSet.of(EstadoGestion.CERRADA_CUMPLIDA, EstadoGestion.CERRADA_INCUMPLIDA, EstadoGestion.ANULADA);

    private final RecomendacionRepository recomendacionRepo;
    private final AlertaRepository alertaRepo;
    private final TelegramNotificationService telegram;

    public AlertaScheduler(RecomendacionRepository recomendacionRepo, AlertaRepository alertaRepo,
                           TelegramNotificationService telegram) {
        this.recomendacionRepo = recomendacionRepo;
        this.alertaRepo = alertaRepo;
        this.telegram = telegram;
    }

    @Scheduled(cron = "${sgs.alertas.cron:0 0 8 * * *}")
    @Transactional
    public void barrer() {
        LocalDate hoy = LocalDate.now();
        List<Recomendacion> activas = recomendacionRepo.findActivasParaAlerta(TERMINALES);
        int generadas = 0;

        for (Recomendacion r : activas) {
            int ventana = r.isGv() ? 7 : 0;  // GV: avisar una semana antes

            // Reloj 1: plazo al destinatario
            LocalDate limitePlazo = limitePlazoDestinatario(r);
            if (limitePlazo != null && !hoy.plusDays(ventana).isBefore(limitePlazo)
                    && !alertaRepo.existsByRecomendacionIdAndTipoAndAtendidaFalse(r.getId(), TipoAlerta.PLAZO_DESTINATARIO)) {
                generar(r, TipoAlerta.PLAZO_DESTINATARIO, limitePlazo);
                generadas++;
            }

            // Reloj 2: estancamiento de fase interna
            LocalDate limiteFase = limiteEstancamientoFase(r);
            if (limiteFase != null && !hoy.plusDays(ventana).isBefore(limiteFase)
                    && !alertaRepo.existsByRecomendacionIdAndTipoAndAtendidaFalse(r.getId(), TipoAlerta.ESTANCAMIENTO_FASE)) {
                generar(r, TipoAlerta.ESTANCAMIENTO_FASE, limiteFase);
                generadas++;
            }
        }
        log.info("Barrido de alertas: {} recomendaciones activas, {} alertas generadas.", activas.size(), generadas);
    }

    private LocalDate limitePlazoDestinatario(Recomendacion r) {
        Oficio o = r.getOficio();
        if (r.getPlazo() == null || o.getFechaIngreso() == null) return null;
        return o.getFechaIngreso().toLocalDate().plus(r.getPlazo().duracion());
    }

    private LocalDate limiteEstancamientoFase(Recomendacion r) {
        Seguimiento ultimo = r.getSeguimientos().isEmpty() ? null : r.getSeguimientos().get(0); // @OrderBy fecha DESC
        FaseSeguimiento fase = ultimo != null && ultimo.getFase() != null ? ultimo.getFase() : FaseSeguimiento.F1_REGISTRO;
        if (fase.offset() == null) return null; // ADMINISTRATIVO no genera estancamiento
        LocalDate base = (ultimo != null && ultimo.getFechaSeguimiento() != null)
                ? ultimo.getFechaSeguimiento()
                : (r.getOficio().getFechaIngreso() != null ? r.getOficio().getFechaIngreso().toLocalDate() : null);
        return base == null ? null : base.plus(fase.offset());
    }

    private void generar(Recomendacion r, TipoAlerta tipo, LocalDate limite) {
        AlertaSeguimiento a = new AlertaSeguimiento();
        a.setRecomendacionId(r.getId());
        a.setTipo(tipo);
        a.setFechaLimite(limite);
        a.setFechaGenerada(LocalDateTime.now());
        boolean notificada = telegram.enviar(mensaje(r, tipo, limite));
        a.setNotificadaTelegram(notificada);
        alertaRepo.save(a);
    }

    private String mensaje(Recomendacion r, TipoAlerta tipo, LocalDate limite) {
        return "<b>[SGS] Alerta de seguimiento</b>\n"
                + "Oficio: " + r.getOficio().getNroOficio() + " (Rec. " + r.getCorrelativo() + ")\n"
                + "Tipo: " + tipo + (r.isGv() ? " | GV" : "") + "\n"
                + "Vence: " + limite;
    }
}
