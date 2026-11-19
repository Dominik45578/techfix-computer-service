package pk.wieik.ti.service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.regex.Pattern;

/** Wspólne reguły dla danych konta: rejestracja, zmiana hasła i edycja profilu sprawdzają je tak samo. */
final class UserValidation {
    static final int MAX_EMAIL_LENGTH = 254;
    static final int MIN_PASSWORD_LENGTH = 12;
    static final int MAX_PASSWORD_LENGTH = 128;
    static final int MAX_NAME_LENGTH = 100;

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern PHONE = Pattern.compile("^\\+?[0-9 ()-]{6,20}$");

    private UserValidation() {}

    static String normalizeEmail(String email) {
        String value = email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
        if (value.isEmpty() || value.length() > MAX_EMAIL_LENGTH) {
            throw new IllegalArgumentException("E-mail musi mieć od 1 do " + MAX_EMAIL_LENGTH + " znaków");
        }
        if (!EMAIL.matcher(value).matches()) {
            throw new IllegalArgumentException("Niepoprawny adres e-mail");
        }
        return value;
    }

    static void validatePassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Hasło musi mieć co najmniej " + MIN_PASSWORD_LENGTH + " znaków");
        }
        if (password.length() > MAX_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Hasło może mieć najwyżej " + MAX_PASSWORD_LENGTH + " znaków");
        }
        if (password.isBlank()) {
            throw new IllegalArgumentException("Hasło nie może składać się z samych spacji");
        }
    }

    static String normalizeName(String name, String label) {
        String value = name == null ? "" : name.trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException(label + " nie może być puste");
        }
        if (value.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(label + " może mieć najwyżej " + MAX_NAME_LENGTH + " znaków");
        }
        return value;
    }

    /** Telefon jest opcjonalny: puste pole zapisujemy jako NULL. */
    static String normalizePhone(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return null;
        }
        String value = phoneNumber.trim();
        if (!PHONE.matcher(value).matches()) {
            throw new IllegalArgumentException("Niepoprawny numer telefonu");
        }
        return value;
    }

    static LocalDate validateDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth != null && dateOfBirth.isAfter(LocalDate.now(ZoneOffset.UTC))) {
            throw new IllegalArgumentException("Data urodzenia nie może być z przyszłości");
        }
        return dateOfBirth;
    }
}
