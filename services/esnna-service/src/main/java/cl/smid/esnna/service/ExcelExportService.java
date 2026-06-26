package cl.smid.esnna.service;

import cl.smid.esnna.domain.RespuestaSiNo;
import cl.smid.esnna.entity.EsnnaEntity;
import cl.smid.esnna.entity.Imputado;
import cl.smid.esnna.exception.EsnnaProcessingException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Exporta la matriz ESNNA a Excel.
 *  - XLS-1: SXSSFWorkbook (streaming a disco) en vez de XSSF, para no cargar miles
 *    de casos en memoria (OOM).
 *  - XLS-5: saneamiento anti formula/CSV injection (prefijos = + - @).
 *  - MOD-3: imputados aplanados desde la relación (nombre/rut/domicilio/sexo;
 *    "SÍ" en funcionario si algún imputado lo es).
 *  - Las columnas IA no van al Excel (decisión de negocio del original); el color
 *    que rige es semaforoFinal, no se exporta como columna.
 */
@Service
public class ExcelExportService {

    private static final DateTimeFormatter F_FECHA = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final int VENTANA_FILAS = 100; // filas en memoria antes de volcar a disco

    private static final String[] HEADERS = {
            "PARA QUERELLA", "REQUERIMIENTO", "N° CORRELATIVO", "Fecha", "N° OFICIO", "CARPETA",
            "REGIÓN", "Tipo de Programa", "Nombre programa o residencia que denuncia",
            "Delito concreto (analisis)", "NNA BAJO CUIDADO ESTADO", "RESIDENCIA",
            "Denunciante", "Contacto",
            "NNA", "Sexo", "Cédula", "Nacionalidad", "Fecha de nacimiento", "edad",
            "Consumo de drogas y/o alcohol", "Curador", "NAD/PMA", "contacto",
            "Imputado conocido", "Sexo posible imputado (a)", "nombre imputado",
            "RUT IMPUTADO", "domicilio imputado", "FUNCIONARIO O EX FUNCIONARIO PUBLICO",
            "Lugar de ocurrencia de hechos", "Comunas involucradas", "HECHOS", "TIPO DE VIOLENCIA",
            "REDES SOCIALES MENCIONADAS", "Identificación de locales, Bares u hoteles",
            "Observación", "QUERELLA", "Denuncias anteriores", "RUC ASOCIADOS",
            "Presunta red de explotación", "Gestiones", "Descripción", "PENDIENTE"
    };

    public ByteArrayInputStream exportarMatrizEsnna(List<EsnnaEntity> casos) {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(VENTANA_FILAS);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            workbook.setCompressTempFiles(true);
            SXSSFSheet sheet = workbook.createSheet("Matriz Consolidada ESNNA");
            CellStyle headerStyle = buildHeaderStyle(workbook);

            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < HEADERS.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERS[col]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (EsnnaEntity c : casos) {
                Row row = sheet.createRow(rowIdx++);
                int i = 0;
                set(row, i++, c.getParaQuerella());
                set(row, i++, c.getRequerimiento());
                set(row, i++, c.getNroCorrelativo());
                set(row, i++, c.getFecha() != null ? c.getFecha().format(F_FECHA) : "");
                set(row, i++, c.getNroOficio());
                set(row, i++, c.getCarpeta());
                set(row, i++, c.getRegion());
                set(row, i++, c.getTipoPrograma());
                set(row, i++, c.getNombreProgramaResidencia());
                set(row, i++, c.getDelitoConcreto());
                set(row, i++, sino(c.getNnaBajoCuidadoEstado()));
                set(row, i++, c.getResidencia());
                set(row, i++, c.getDenunciante());
                set(row, i++, c.getContactoDenunciante());
                set(row, i++, c.getNna());
                set(row, i++, c.getSexoNna() != null ? c.getSexoNna().name() : "");
                set(row, i++, c.getCedulaNna());
                set(row, i++, c.getNacionalidadNna());
                set(row, i++, c.getFechaNacimiento() != null ? c.getFechaNacimiento().format(F_FECHA) : "");
                set(row, i++, c.getEdad() != null ? String.valueOf(c.getEdad()) : "");
                set(row, i++, c.getConsumoDrogasAlcohol());
                set(row, i++, c.getCurador());
                set(row, i++, c.getNadPma());
                set(row, i++, c.getContactoNadPma());
                set(row, i++, sino(c.getImputadoConocido()));
                // Imputados aplanados desde la relación
                List<Imputado> imps = c.getImputados();
                set(row, i++, aplanar(imps, Imputado::getSexo));
                set(row, i++, aplanar(imps, Imputado::getNombre));
                set(row, i++, aplanar(imps, Imputado::getRut));
                set(row, i++, aplanar(imps, Imputado::getDomicilio));
                set(row, i++, algunFuncionario(imps));
                set(row, i++, c.getLugarOcurrenciaHechos());
                set(row, i++, c.getComunasInvolucradas());
                set(row, i++, c.getHechos());
                set(row, i++, c.getTipoViolencia());
                set(row, i++, join(c.getRedesSocialesMencionadas()));
                set(row, i++, c.getIdentificacionLocalesBaresHoteles());
                set(row, i++, c.getObservacion());
                set(row, i++, c.getQuerella());
                set(row, i++, c.getDenunciasAnteriores());
                set(row, i++, c.getRucAsociados());
                set(row, i++, sino(c.getPresuntaRedExplotacion()));
                set(row, i++, c.getGestiones());
                set(row, i++, c.getDescripcion());
                set(row, i, c.getPendiente());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new EsnnaProcessingException("Error al generar el Excel de la matriz ESNNA.", e);
        }
    }

    private CellStyle buildHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private void set(Row row, int col, String value) {
        row.createCell(col).setCellValue(sanitizar(value));
    }

    /** XLS-5: neutraliza fórmulas si el valor empieza con = + - @. */
    private String sanitizar(String v) {
        if (v == null || v.isEmpty()) return "";
        char c0 = v.charAt(0);
        if (c0 == '=' || c0 == '+' || c0 == '-' || c0 == '@') {
            return "'" + v;
        }
        return v;
    }

    private String sino(RespuestaSiNo r) {
        return r != null ? r.getEtiqueta() : "";
    }

    private String join(List<String> list) {
        return (list == null || list.isEmpty()) ? "" : String.join(", ", list);
    }

    private String aplanar(List<Imputado> imps, Function<Imputado, String> campo) {
        if (imps == null || imps.isEmpty()) return "";
        return imps.stream()
                .map(campo)
                .map(s -> s != null && !s.isBlank() ? s : "—")
                .collect(Collectors.joining(", "));
    }

    private String algunFuncionario(List<Imputado> imps) {
        if (imps == null || imps.isEmpty()) return "";
        boolean alguno = imps.stream().anyMatch(i -> i.getEsFuncionarioPublico() == RespuestaSiNo.SI);
        return alguno ? "SÍ" : "NO";
    }
}