package pk.wieik.ti.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pk.wieik.ti.controller.dto.LoginRequest;
import pk.wieik.ti.controller.dto.RegisterRequest;
import pk.wieik.ti.controller.dto.TokenResponse;
import pk.wieik.ti.controller.dto.UserResponse;
import pk.wieik.ti.model.UserRole;
import pk.wieik.ti.security.token.IssuedToken;
import pk.wieik.ti.service.AuthenticatedUser;

import java.io.IOException;

@WebServlet("/api/auth/*")
public class AuthServlet extends JsonServlet {
    private static final AuthenticatedUser PLACEHOLDER_USER =
            new AuthenticatedUser(0, "placeholder@example.com", "Placeholder User", UserRole.CLIENT);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        switch (endpoint(request)) {
            case "/login" -> login(request, response);
            case "/register" -> register(request, response);
            case "/refresh" -> refresh(request, response);
            case "/logout" -> logout(request, response);
            default -> throw new ApiException(HttpServletResponse.SC_NOT_FOUND, "Nie znaleziono zasobu");
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        readJson(request, LoginRequest.class);
        sendTokens(request, response, PLACEHOLDER_USER);
    }

    private void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RegisterRequest body = readJson(request, RegisterRequest.class);
        sendJson(response, HttpServletResponse.SC_CREATED,
                new UserResponse(0, body.email().trim(), body.firstName() + " " + body.lastName(), UserRole.CLIENT));
    }

    private void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean valid = RefreshTokenCookie.read(request)
                .flatMap(app().refreshTokens()::verify)
                .isPresent();
        if (!valid) {
            RefreshTokenCookie.clear(request, response);
            throw new ApiException(HttpServletResponse.SC_UNAUTHORIZED, "Sesja wygasła, zaloguj się ponownie");
        }
        sendTokens(request, response, PLACEHOLDER_USER);
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) {
        RefreshTokenCookie.clear(request, response);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private void sendTokens(HttpServletRequest request, HttpServletResponse response, AuthenticatedUser user)
            throws IOException {
        IssuedToken accessToken = app().accessTokens().issue(user);
        RefreshTokenCookie.write(request, response, app().refreshTokens().issue(user.id()));
        response.setHeader("Cache-Control", "no-store");
        sendJson(response, HttpServletResponse.SC_OK, TokenResponse.bearer(accessToken));
    }

    private static String endpoint(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        return pathInfo == null ? "" : pathInfo;
    }
}
