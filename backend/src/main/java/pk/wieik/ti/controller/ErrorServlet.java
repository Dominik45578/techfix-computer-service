package pk.wieik.ti.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Globalna strona błędu (zarejestrowana w {@code web.xml}): zamiast HTML-a Tomcata
 * każdy błąd, czyli 404, 405 albo nieobsłużony wyjątek (500), zwraca JSON
 * {@code {"status": 404, "message": "..."}}.
 */
@WebServlet("/error")
public class ErrorServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object code = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int status = code instanceof Integer value ? value : HttpServletResponse.SC_NOT_FOUND;
        String detail = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        Json.send(response, status,
                "{\"status\":" + status + ",\"message\":" + Json.quote(messageFor(status, detail)) + "}");
    }

    /** Szczegóły pokazujemy tylko dla błędów klienta; przy 5xx nie ujawniamy treści wyjątku. */
    private static String messageFor(int status, String detail) {
        if (status < 500 && detail != null && !detail.isBlank()) {
            return detail;
        }
        return switch (status) {
            case 400 -> "Nieprawidłowe żądanie";
            case 401 -> "Wymagane uwierzytelnienie";
            case 403 -> "Brak uprawnień";
            case 404 -> "Nie znaleziono zasobu";
            case 405 -> "Metoda HTTP nieobsługiwana";
            default -> status < 500 ? "Błąd żądania" : "Wewnętrzny błąd serwera";
        };
    }
}
