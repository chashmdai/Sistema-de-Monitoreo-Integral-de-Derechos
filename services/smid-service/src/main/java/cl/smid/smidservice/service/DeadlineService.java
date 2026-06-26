package cl.smid.smidservice.service;

import cl.smid.smidservice.entity.EstadoOficio;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class DeadlineService {

    public LocalDate calcularVencimientoHabiles(LocalDate fechaEnvio, int diasPlazo) {
        LocalDate fecha = fechaEnvio;
        int diasAgregados = 0;

        while (diasAgregados < diasPlazo) {
            fecha = fecha.plusDays(1);
            if (!(fecha.getDayOfWeek() == DayOfWeek.SATURDAY || fecha.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                diasAgregados++;
            }
        }
        return fecha;
    }

    public EstadoOficio calcularEstadoActual(LocalDate vencimiento) {
        LocalDate hoy = LocalDate.now();

        if (hoy.isAfter(vencimiento)) {
            return EstadoOficio.VENCIDO;
        }

        long diasRestantes = ChronoUnit.DAYS.between(hoy, vencimiento);

        if (diasRestantes <= 2 && diasRestantes >= 0) {
            return EstadoOficio.POR_VENCER;
        }

        return EstadoOficio.EN_PLAZO;
    }
}
