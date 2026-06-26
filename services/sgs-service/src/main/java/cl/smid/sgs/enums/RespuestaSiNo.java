package cl.smid.sgs.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** SÍ/NO tipado. Serializa como "SÍ"/"NO" y tolera variantes al deserializar (ENT-6). */
public enum RespuestaSiNo {
    SI("SÍ"), NO("NO");

    private final String label;
    RespuestaSiNo(String label) { this.label = label; }

    @JsonValue
    public String getLabel() { return label; }

    @JsonCreator
    public static RespuestaSiNo from(String value) {
        if (value == null || value.isBlank()) return null;
        String n = value.trim().toUpperCase();
        return switch (n) {
            case "SI", "SÍ", "S", "TRUE", "1" -> SI;
            case "NO", "N", "FALSE", "0" -> NO;
            default -> throw new IllegalArgumentException("Valor RespuestaSiNo inválido: " + value);
        };
    }
}
