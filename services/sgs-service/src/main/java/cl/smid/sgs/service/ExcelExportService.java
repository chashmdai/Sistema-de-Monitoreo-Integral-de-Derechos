package cl.smid.sgs.service;

import cl.smid.sgs.entity.Catalogo;
import cl.smid.sgs.entity.Recomendacion;
import cl.smid.sgs.entity.Seguimiento;
import cl.smid.sgs.repository.SeguimientoRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

/** Export a Excel: una fila por Seguimiento (decisión #16). SXSSF streaming + saneo de fórmula (XLS-5). */
@Service
public class ExcelExportService {

    private static final String[] HEADERS = {
            "N° Oficio", "Región", "Institución", "Residencia/Centro", "Nivel",
            "Correlativo", "Dimensión", "Materia", "Categoría", "Nudo Crítico",
            "Tipo Recomendación", "Verbo", "Descripción", "Plazo", "GV",
            "Estado Gestión", "Fase", "Fecha Seguimiento", "Tipo Seguimiento",
            "Fecha Respuesta", "Tipo Respuesta", "Evaluación IA", "Evaluación Final",
            "Autor Final", "Confianza", "Requiere Revisión", "Valoración Rúbrica",
            "Responsable", "Fecha Ingreso"
    };
    private static final DateTimeFormatter D = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final SeguimientoRepository seguimientoRepo;
    public ExcelExportService(SeguimientoRepository seguimientoRepo) { this.seguimientoRepo = seguimientoRepo; }

    @Transactional(readOnly = true)
    public byte[] exportar() {
        try (SXSSFWorkbook wb = new SXSSFWorkbook(100); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Seguimiento SGS");

            CellStyle headerStyle = wb.createCellStyle();
            Font bold = wb.createFont();
            bold.setBold(true);
            headerStyle.setFont(bold);

            Row header = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell c = header.createCell(i);
                c.setCellValue(HEADERS[i]);
                c.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (Seguimiento s : seguimientoRepo.findAllForExport()) {
                Recomendacion r = s.getRecomendacion();
                var o = r.getOficio();
                Row row = sheet.createRow(rowIdx++);
                int col = 0;
                set(row, col++, o.getNroOficio());
                set(row, col++, o.getRegion());
                set(row, col++, o.getInstitucion());
                set(row, col++, o.getResidenciaCentro());
                set(row, col++, o.getNivel());
                set(row, col++, r.getCorrelativo());
                set(row, col++, r.getDimension());
                set(row, col++, etiqueta(r.getMateria()));
                set(row, col++, etiqueta(r.getCategoria()));
                set(row, col++, r.getNudoCritico());
                set(row, col++, r.getTipoRecomendacion());
                set(row, col++, r.getVerbo());
                set(row, col++, r.getDescripcion());
                set(row, col++, r.getPlazo() == null ? null : r.getPlazo().getLabel());
                set(row, col++, r.isGv() ? "SÍ" : "NO");
                set(row, col++, r.getEstado() == null ? null : r.getEstado().name());
                set(row, col++, s.getFase() == null ? null : s.getFase().getLabel());
                set(row, col++, s.getFechaSeguimiento() == null ? null : s.getFechaSeguimiento().format(D));
                set(row, col++, etiqueta(s.getTipoSeguimiento()));
                set(row, col++, s.getFechaRespuesta() == null ? null : s.getFechaRespuesta().format(D));
                set(row, col++, etiqueta(s.getTipoRespuesta()));
                set(row, col++, s.getEvaluacionIA() == null ? null : s.getEvaluacionIA().getLabel());
                set(row, col++, s.getEvaluacionFinal() == null ? null : s.getEvaluacionFinal().getLabel());
                set(row, col++, s.getEvaluacionFinalAutor());
                set(row, col++, s.getConfianza() == null ? null : s.getConfianza().toString());
                set(row, col++, s.isRequiereRevisionHumana() ? "SÍ" : "NO");
                set(row, col++, s.getValoracionRubrica());
                set(row, col++, s.getResponsableSeguimiento());
                set(row, col++, o.getFechaIngreso() == null ? null : o.getFechaIngreso().format(DT));
            }

            wb.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo generar el Excel.", e);
        }
    }

    private String etiqueta(Catalogo c) { return c == null ? null : c.getEtiqueta(); }

    private void set(Row row, int col, String value) {
        row.createCell(col).setCellValue(sanitize(value));
    }

    /** Neutraliza inyección de fórmulas en celdas de texto provenientes de IA (XLS-5). */
    private String sanitize(String value) {
        if (value == null) return "";
        char f = value.isEmpty() ? '\0' : value.charAt(0);
        if (f == '=' || f == '+' || f == '-' || f == '@' || f == '\t' || f == '\r') {
            return "'" + value;
        }
        return value;
    }
}
