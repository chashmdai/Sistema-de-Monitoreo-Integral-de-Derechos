package cl.smid.esnna.service;

import cl.smid.esnna.entity.EsnnaEntity;
import cl.smid.esnna.entity.Imputado;
import cl.smid.esnna.exception.EsnnaProcessingException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Informe PDF por caso (FEAT expediente): entregable individual para adjuntar a
 * la carpeta, con el análisis, el semáforo y su justificación. Incluye pie de
 * confidencialidad por tratarse de datos de NNA.
 */
@Service
public class InformeService {

    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final float MARGEN = 50;
    private static final float ANCHO = PDRectangle.A4.getWidth() - 2 * MARGEN;
    private static final float LEADING = 15;

    public byte[] generar(EsnnaEntity c) {
        try (PDDocument doc = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Cursor cur = new Cursor(doc);

            cur.titulo("Informe de Caso ESNNA");
            cur.sub("Defensoría de la Niñez — Protocolo PR-PDR-05");
            cur.salto();

            cur.h("Priorización");
            cur.kv("Semáforo final", c.getSemaforoFinal() != null ? c.getSemaforoFinal().name() : "—");
            cur.kv("Semáforo IA", c.getSemaforoIa() != null ? c.getSemaforoIa().name() : "—");
            cur.kv("Confianza IA", c.getConfianzaIa() != null ? String.valueOf(c.getConfianzaIa()) : "—");
            cur.parrafo("Justificación: " + nz(c.getJustificacionIa()));
            cur.salto();

            cur.h("Identificación");
            cur.kv("N° Oficio", nz(c.getNroOficio()));
            cur.kv("RUC asociados", nz(c.getRucAsociados()));
            cur.kv("Región", nz(c.getRegion()));
            cur.kv("Fecha", c.getFecha() != null ? c.getFecha().format(F) : "—");
            cur.kv("Estado de gestión", c.getEstadoGestion() != null ? c.getEstadoGestion().name() : "—");
            cur.salto();

            cur.h("NNA");
            cur.kv("Nombre", nz(c.getNna()));
            cur.kv("Cédula", nz(c.getCedulaNna()));
            cur.kv("Edad", c.getEdad() != null ? String.valueOf(c.getEdad()) : "—");
            cur.kv("Sexo", c.getSexoNna() != null ? c.getSexoNna().name() : "—");
            cur.kv("Nacionalidad", nz(c.getNacionalidadNna()));
            cur.salto();

            cur.h("Delito");
            cur.kv("Delito concreto", nz(c.getDelitoConcreto()));
            cur.kv("Tipo de violencia", nz(c.getTipoViolencia()));
            cur.salto();

            cur.h("Imputados");
            List<Imputado> imps = c.getImputados();
            if (imps == null || imps.isEmpty()) {
                cur.parrafo("Sin imputados registrados.");
            } else {
                int n = 1;
                for (Imputado im : imps) {
                    String linea = n++ + ". " + nz(im.getNombre()) + " | RUT: " + nz(im.getRut())
                            + " | Func. público: " + (im.getEsFuncionarioPublico() != null ? im.getEsFuncionarioPublico().getEtiqueta() : "—");
                    cur.parrafo(linea);
                }
            }
            cur.salto();

            cur.h("Hechos");
            cur.parrafo(nz(c.getHechos()));

            cur.cerrar();

            cur.pieConfidencialidad();
            doc.save(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new EsnnaProcessingException("No se pudo generar el informe PDF del caso.", e);
        }
    }

    private static String nz(String s) {
        return (s == null || s.isBlank()) ? "—" : s;
    }

    /** Cursor simple con salto de página automático y wrapping de texto. */
    private static final class Cursor {
        private final PDDocument doc;
        private PDPage page;
        private PDPageContentStream cs;
        private float y;
        private final List<PDPage> paginas = new ArrayList<>();

        Cursor(PDDocument doc) throws Exception {
            this.doc = doc;
            nuevaPagina();
        }

        private void nuevaPagina() throws Exception {
            if (cs != null) cs.close();
            page = new PDPage(PDRectangle.A4);
            doc.addPage(page);
            paginas.add(page);
            cs = new PDPageContentStream(doc, page);
            y = PDRectangle.A4.getHeight() - MARGEN;
        }

        private void asegurar(float necesario) throws Exception {
            if (y - necesario < MARGEN) nuevaPagina();
        }

        void titulo(String t) throws Exception { linea(t, PDType1Font.HELVETICA_BOLD, 16); }
        void sub(String t) throws Exception { linea(t, PDType1Font.HELVETICA, 10); }
        void h(String t) throws Exception { salto(); linea(t, PDType1Font.HELVETICA_BOLD, 12); }
        void kv(String k, String v) throws Exception { linea(k + ": " + v, PDType1Font.HELVETICA, 11); }
        void salto() throws Exception { asegurar(LEADING); y -= LEADING; }

        void parrafo(String texto) throws Exception {
            for (String l : wrap(texto, PDType1Font.HELVETICA, 11, ANCHO)) {
                linea(l, PDType1Font.HELVETICA, 11);
            }
        }

        private void linea(String texto, PDType1Font font, float size) throws Exception {
            asegurar(LEADING);
            cs.beginText();
            cs.setFont(font, size);
            cs.newLineAtOffset(MARGEN, y);
            cs.showText(sanitizar(texto));
            cs.endText();
            y -= LEADING;
        }

        void pieConfidencialidad() throws Exception {
            for (PDPage p : paginas) {
                try (PDPageContentStream pie = new PDPageContentStream(doc, p, PDPageContentStream.AppendMode.APPEND, true, true)) {
                    pie.beginText();
                    pie.setFont(PDType1Font.HELVETICA_OBLIQUE, 8);
                    pie.newLineAtOffset(MARGEN, MARGEN - 20);
                    pie.showText("Documento confidencial. Contiene datos personales de NNA (Ley 19.628 / 21.719). Uso institucional restringido.");
                    pie.endText();
                }
            }
        }

        void cerrar() throws Exception { if (cs != null) cs.close(); }

        private List<String> wrap(String texto, PDType1Font font, float size, float maxAncho) throws Exception {
            List<String> out = new ArrayList<>();
            if (texto == null || texto.isBlank()) { out.add("—"); return out; }
            for (String parrafo : texto.split("\n")) {
                StringBuilder linea = new StringBuilder();
                for (String palabra : parrafo.split(" ")) {
                    String prueba = linea.isEmpty() ? palabra : linea + " " + palabra;
                    float ancho = font.getStringWidth(sanitizar(prueba)) / 1000 * size;
                    if (ancho > maxAncho && !linea.isEmpty()) {
                        out.add(linea.toString());
                        linea = new StringBuilder(palabra);
                    } else {
                        linea = new StringBuilder(prueba);
                    }
                }
                if (!linea.isEmpty()) out.add(linea.toString());
            }
            return out;
        }

        /** PDType1Font (WinAnsi) no soporta todo Unicode; se limpia lo no representable. */
        private String sanitizar(String s) {
            return s == null ? "" : s.replaceAll("[^\\x00-\\xFF]", "?");
        }
    }
}
