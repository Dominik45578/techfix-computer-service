package pk.wieik.ti.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pk.wieik.ti.config.AppContext;
import pk.wieik.ti.service.AuthenticatedUser;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;

/**
 * Baza wszystkich kontrolerów REST.
 */
abstract class JsonServlet extends HttpServlet {
    private AppContext app;

    @Override
    public void init() throws ServletException {
        app = AppContext.from(getServletContext());
    }

    protected AppContext app() {
        return app;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            super.service(request, response);
        } catch (ApiException ex) {
            sendErrorOrRethrow(request, response, ex.status(), ex);
        } catch (IllegalArgumentException ex) {
            sendErrorOrRethrow(request, response, HttpServletResponse.SC_BAD_REQUEST, ex);
        }
    }

    protected void sendJson(HttpServletResponse response, int status, Object body) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        app.json().writeValue(response.getOutputStream(), body);
    }

    protected void sendError(HttpServletResponse response, int status, String message, String path)
            throws IOException {
        sendJson(response, status, ApiError.of(status, message, path));
    }

    /**
     * Czyta treść żądania jako {@code type}. Niepoprawny JSON albo zła struktura daje 400.
     * Jeśli konstruktor rekordu odrzuci dane (np. {@code Checks.notBlank}), klient dostaje jego komunikat.
     */
    protected <T> T readJson(HttpServletRequest request, Class<T> type) throws IOException {
        try {
            return app.json().readValue(request.getInputStream(), type);
        } catch (JacksonException ex) {
            if (ex.getCause() instanceof IllegalArgumentException invalid) {
                throw ApiException.badRequest(invalid.getMessage());
            }
            throw ApiException.badRequest("Treść żądania nie jest poprawnym JSON-em albo ma złą strukturę");
        }
    }

    /** Użytkownik z tokenu (ustawiony przez {@code AuthenticationFilter}); bez ważnego tokenu 401. */
    protected AuthenticatedUser currentUser(HttpServletRequest request) {
        if (request.getUserPrincipal() instanceof AuthenticatedUser user) {
            return user;
        }
        throw new ApiException(HttpServletResponse.SC_UNAUTHORIZED, "Wymagane uwierzytelnienie");
    }

    /** Gdy część odpowiedzi już poszła do klienta, nie da się podmienić jej na błąd. */
    private void sendErrorOrRethrow(HttpServletRequest request, HttpServletResponse response,
                                    int status, RuntimeException ex) throws IOException {
        if (response.isCommitted()) {
            throw ex;
        }
        response.resetBuffer();
        sendError(response, status, ex.getMessage(), request.getRequestURI());
    }
}
