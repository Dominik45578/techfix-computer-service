package pk.wieik.ti.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pk.wieik.ti.controller.dto.LoginRequest;
import pk.wieik.ti.controller.dto.RegisterRequest;
import pk.wieik.ti.controller.dto.UserResponse;
import pk.wieik.ti.security.token.RefreshTokenRotation;
import pk.wieik.ti.service.AuthenticatedUser;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/api/auth/*")
public class AuthServlet extends JsonServlet {
    private static final String INVALID_CREDENTIALS = "Nieprawidłowy e-mail lub hasło";
    private static final String SESSION_EXPIRED = "Sesja wygasła, zaloguj się ponownie";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        switch (endpoint(request)) {
            case "/login" -> login(request, response);
            case "/register" -> register(request, response);
            case "/refresh" -> refresh(request, response);
            case "/logout" -> logout(request, response);
            case "/forgot-password", "/reset-password" -> passwordResetNotImplemented();
            default -> throw new ApiException(HttpServletResponse.SC_NOT_FOUND, "Nie znaleziono zasobu");
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LoginRequest body = readJson(request, LoginRequest.class);
        AuthenticatedUser user = app().auth().authenticate(body.email(), body.password())
                .orElseThrow(() -> new ApiException(HttpServletResponse.SC_UNAUTHORIZED, INVALID_CREDENTIALS));
        sendTokens(request, response, user, app().refreshTokens().issue(user.id()));
    }

    private void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RegisterRequest body = readJson(request, RegisterRequest.class);
        AuthenticatedUser user = app().auth().registerClient(body.toRegistrationRequest());
        sendJson(response, HttpServletResponse.SC_CREATED, UserResponse.from(user));
    }

    private void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Optional<RefreshTokenRotation> rotation = RefreshTokenCookie.read(request)
                .flatMap(app().refreshTokens()::rotate);
        Optional<AuthenticatedUser> user = rotation
                .flatMap(rotated -> app().auth().findUser(rotated.userId()));
        if (user.isEmpty()) {
            RefreshTokenCookie.clear(request, response);
            throw new ApiException(HttpServletResponse.SC_UNAUTHORIZED, SESSION_EXPIRED);
        }
        sendTokens(request, response, user.get(), rotation.get().refreshToken());
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) {
        RefreshTokenCookie.read(request).ifPresent(app().refreshTokens()::revoke);
        RefreshTokenCookie.clear(request, response);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    // TODO: reset hasła.
    //  POST /forgot-password {email}: zawsze 202 (bez zdradzania, czy konto istnieje), wysyłka maila z jednorazowym tokenem.
    //  POST /reset-password {token, newPassword}: weryfikacja hasha tokena, zmiana hasła, refreshTokens().revokeAll(userId).
    //  Wymaga: tabeli password_reset_tokens (migracja Flyway) i serwisu do wysyłki maili.
    private static void passwordResetNotImplemented() {
        throw new ApiException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Reset hasła nie jest jeszcze dostępny");
    }
}
