package pk.wieik.ti.controller.dto;

import java.util.Objects;

final class RequestValidator {
    private RequestValidator() {}

    static void notBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Pole „" + field + "” nie może być puste");
        }
    }

    static void equals(String value, String other, String field, String otherField) {
        if (!Objects.equals(value, other)) {
            throw new IllegalArgumentException("Pola „" + field + "” i „" + otherField + "” muszą być takie same");
        }
    }
}
