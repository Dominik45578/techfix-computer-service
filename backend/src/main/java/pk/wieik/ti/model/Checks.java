package pk.wieik.ti.model;

final class Checks {
    private Checks() {}

    static String notBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Pole „" + field + "” nie może być puste");
        }
        return value;
    }

    static int nonNegative(int value, String field) {
        if (value < 0) {
            throw new IllegalArgumentException("Pole „" + field + "” nie może być ujemne");
        }
        return value;
    }

    static int inRange(int value, int min, int max, String field) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                    "Pole „" + field + "” musi mieć wartość od " + min + " do " + max);
        }
        return value;
    }
}
