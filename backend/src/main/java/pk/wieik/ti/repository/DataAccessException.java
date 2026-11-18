package pk.wieik.ti.repository;

/**
 * Błąd warstwy danych zgłaszany przez adaptery portów.
 * Dzięki niemu serwisy i kontrolery nie zależą od {@code java.sql.SQLException}.
 */
public class DataAccessException extends RuntimeException {
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
