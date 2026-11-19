package pk.wieik.ti.controller.dto;

final class RequestValidator {
    private RequestValidator() {}

    static void notBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Pole „" + field + "” nie może być puste");
        }
    }
}
