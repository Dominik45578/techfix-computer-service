package pk.wieik.ti.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pk.wieik.ti.model.UserRole;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/api/auth/*")
public class AuthServlet extends JsonServlet {

    public record LoginRequest(String email, String password) {}

    public record RegisterRequest(String email, String password, String firstName, String lastName,
                                  String phoneNumber, LocalDate dateOfBirth) {}

    public record TokenResponse(String accessToken, String tokenType, long expiresIn) {}

    private static final long ACCESS_TOKEN_TTL_SECONDS = 15 * 60;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        switch (endpoint(request)) {
            case "/login" -> login(request, response);
            case "/register" -> register(request, response);
            case "/refresh" -> refresh(request, response);
            case "/logout" -> logout(request, response);
            default -> throw notFound();
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LoginRequest body = readJson(request, LoginRequest.class);
        requireNotBlank(body.email(), "email");
        requireNotBlank(body.password(), "password");

        // TODO: AuthService.authenticate → TokenService (JWT) + refresh token w ciasteczku HttpOnly
        sendJson(response, HttpServletResponse.SC_OK, stubToken());
    }

    private void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RegisterRequest body = readJson(request, RegisterRequest.class);
        requireNotBlank(body.email(), "email");
        requireNotBlank(body.password(), "password");
        requireNotBlank(body.firstName(), "firstName");
        requireNotBlank(body.lastName(), "lastName");

        // TODO: AuthService.register(..., UserRole.CLIENT)
        sendJson(response, HttpServletResponse.SC_CREATED,
                new UserResponse(0, body.email().trim(), body.firstName() + " " + body.lastName(), UserRole.CLIENT));
    }

    private void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO: odczyt ciasteczka z refresh tokenem, weryfikacja w DB, rotacja
        sendJson(response, HttpServletResponse.SC_OK, stubToken());
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private static TokenResponse stubToken() {
        return new TokenResponse("stub-access-token", "Bearer", ACCESS_TOKEN_TTL_SECONDS);
    }

    /** {@code /api/auth/login} → {@code "/login"}; samo {@code /api/auth} → {@code ""}. */
    private static String endpoint(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        return pathInfo == null ? "" : pathInfo;
    }

    private static void requireNotBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw ApiException.badRequest("Pole „" + field + "” nie może być puste");
        }
    }

    private static ApiException notFound() {
        return new ApiException(HttpServletResponse.SC_NOT_FOUND, "Nie znaleziono zasobu");
    }
}
