package pk.wieik.ti.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Globalna strona błędu (zarejestrowana w {@code web.xml}). Obsługuje to, czego nie złapał
 * {@link JsonServlet}: brak zasobu (404), złą metodę (405) i nieobsłużone wyjątki (500).
 * Zawsze zwraca {@link ApiError} z ogólnym komunikatem, bez szczegółów z kontenera.
 * Własny komunikat można przekazać tylko przez {@link #send}, np. z filtrów bezpieczeństwa.
 */
@WebServlet("/error")
public class ErrorServlet extends JsonServlet {
    private static final String MESSAGE_ATTRIBUTE = ErrorServlet.class.getName() + ".message";

    /**
     * {@code response.sendError(status)} z komunikatem dla klienta. Nie używamy standardowego
     * {@code ERROR_MESSAGE}, bo kontener wkłada tam też swoje teksty (po angielsku) i treść wyjątków przy 500.
     */
    public static void send(HttpServletRequest request, HttpServletResponse response, int status, String message)
            throws IOException {
        request.setAttribute(MESSAGE_ATTRIBUTE, message);
        response.sendError(status);
    }

    /** Wszystkie metody HTTP: przekierowanie na stronę błędu zachowuje metodę oryginalnego żądania. */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) instanceof Integer code
                ? code
                : HttpServletResponse.SC_NOT_FOUND;
        String path = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI) instanceof String uri
                ? uri
                : request.getRequestURI();

        String message = request.getAttribute(MESSAGE_ATTRIBUTE) instanceof String custom
                ? custom
                : messageFor(status);

        sendError(response, status, message, path);
    }

    private static String messageFor(int status) {
        return switch (status) {
            case HttpServletResponse.SC_BAD_REQUEST -> "Nieprawidłowe żądanie";
            case HttpServletResponse.SC_UNAUTHORIZED -> "Wymagane uwierzytelnienie";
            case HttpServletResponse.SC_FORBIDDEN -> "Brak uprawnień";
            case HttpServletResponse.SC_NOT_FOUND -> "Nie znaleziono zasobu";
            case HttpServletResponse.SC_METHOD_NOT_ALLOWED -> "Metoda HTTP nieobsługiwana";
            default -> status < 500 ? "Błąd żądania" : "Wewnętrzny błąd serwera";
        };
    }
}
