package cl.smid.sgs.service;

import cl.smid.sgs.entity.Oficio;
import cl.smid.sgs.entity.Recomendacion;
import cl.smid.sgs.entity.Seguimiento;
import cl.smid.sgs.exception.SgsNotFoundException;
import cl.smid.sgs.repository.OficioRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;

/** Informe de expediente por oficio (PDFBox 3.x). Documento de respaldo para defensa/auditoría. */
@Service
public class InformeService {

    private final OficioRepository oficioRepo;
    public InformeService(OficioRepository oficioRepo) { this.oficioRepo = oficioRepo; }

    @Transactional(readOnly = true)
    public byte[] generar(Long oficioId) {
        Oficio o = oficioRepo.findById(oficioId)
                .orElseThrow(() -> new SgsNotFoundException("Oficio inexistente: " + oficioId));
        try (PDDocument doc = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            var fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            var font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);
            float margin = 50, y = page.getMediaBox().getHeight() - margin;
            PDPageContentStream cs = new PDPageContentStream(doc, page);

            y = linea(cs, fontBold, 16, margin, y, "Informe de Seguimiento - Defensoría de la Niñez");
            y = linea(cs, font, 11, margin, y - 6, "Oficio: " + nz(o.getNroOficio()));
            y = linea(cs, font, 11, margin, y, "Región: " + nz(o.getRegion()) + "  |  Institución: " + nz(o.getInstitucion()));
            y -= 10;

            for (Recomendacion r : o.getRecomendaciones()) {
                if (y < margin + 80) { cs.close(); page = new PDPage(PDRectangle.A4); doc.addPage(page);
                    cs = new PDPageContentStream(doc, page); y = page.getMediaBox().getHeight() - margin; }
                y = linea(cs, fontBold, 12, margin, y - 8,
                        "Rec. " + nz(r.getCorrelativo()) + " - " + nz(r.getDimension()) + " [" + r.getEstado() + "]");
                y = linea(cs, font, 10, margin, y, "Plazo: " + (r.getPlazo() == null ? "N/D" : r.getPlazo().getLabel())
                        + (r.isGv() ? "  |  GV: SÍ" : ""));
                y = linea(cs, font, 10, margin, y, "Nudo crítico: " + recortar(nz(r.getNudoCritico()), 110));
                for (Seguimiento s : r.getSeguimientos()) {
                    if (y < margin + 40) { cs.close(); page = new PDPage(PDRectangle.A4); doc.addPage(page);
                        cs = new PDPageContentStream(doc, page); y = page.getMediaBox().getHeight() - margin; }
                    String ev = s.getEvaluacionFinal() != null ? s.getEvaluacionFinal().getLabel()
                            : (s.getEvaluacionIA() != null ? s.getEvaluacionIA().getLabel() + " (IA)" : "N/D");
                    y = linea(cs, font, 9, margin + 12, y, "- " + (s.getFechaSeguimiento() == null ? "" : s.getFechaSeguimiento())
                            + "  " + (s.getFase() == null ? "" : s.getFase().getLabel()) + "  ->  " + ev);
                }
            }
            cs.close();
            doc.save(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo generar el informe PDF.", e);
        }
    }

    private float linea(PDPageContentStream cs, PDType1Font font, float size, float x, float y, String text) throws Exception {
        cs.beginText();
        cs.setFont(font, size);
        cs.newLineAtOffset(x, y);
        cs.showText(text == null ? "" : text.replaceAll("[\\p{Cntrl}]", " "));
        cs.endText();
        return y - (size + 6);
    }

    private String nz(String s) { return s == null ? "" : s; }
    private String recortar(String s, int max) { return s.length() <= max ? s : s.substring(0, max) + "..."; }
}
