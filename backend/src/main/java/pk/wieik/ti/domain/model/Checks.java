package pk.wieik.ti.domain.model;

import java.math.BigDecimal;

/** Wspólne walidacje pól encji, wywoływane w kompaktowych konstruktorach rekordów. */
public final class Checks {
    private Checks() {}

    public static <T> T notNull(T value, String field) {
        if (value == null) {
            throw new IllegalArgumentException("Pole „" + field + "” nie może być puste");
        }
        return value;
    }

    public static String notBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Pole „" + field + "” nie może być puste");
        }
        return value;
    }

    public static int nonNegative(int value, String field) {
        if (value < 0) {
            throw new IllegalArgumentException("Pole „" + field + "” nie może być ujemne");
        }
        return value;
    }

    public static BigDecimal nonNegative(BigDecimal value, String field) {
        notNull(value, field);
        if (value.signum() < 0) {
            throw new IllegalArgumentException("Pole „" + field + "” nie może być ujemne");
        }
        return value;
    }

    public static int positive(int value, String field) {
        if (value <= 0) {
            throw new IllegalArgumentException("Pole „" + field + "” musi być większe od zera");
        }
        return value;
    }

    public static long positiveId(long value, String field) {
        if (value <= 0) {
            throw new IllegalArgumentException("Pole „" + field + "” musi wskazywać istniejący rekord");
        }
        return value;
    }

    public static int inRange(int value, int min, int max, String field) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                    "Pole „" + field + "” musi mieć wartość od " + min + " do " + max);
        }
        return value;
    }
}
