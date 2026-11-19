package pk.wieik.ti.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pk.wieik.ti.controller.dto.ChangePasswordRequest;
import pk.wieik.ti.controller.dto.ProfileResponse;
import pk.wieik.ti.controller.dto.UpdateProfileRequest;
import pk.wieik.ti.model.User;
import pk.wieik.ti.service.AuthenticatedUser;

import java.io.IOException;

@WebServlet({"/api/me", "/api/me/*"})
public class MeServlet extends JsonServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        switch (endpoint(request)) {
            case "" -> getProfile(request, response);
            default -> throw notFound();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        switch (endpoint(request)) {
            case "" -> updateProfile(request, response);
            case "/password" -> changePassword(request, response);
            default -> throw notFound();
        }
    }

    private void getProfile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = app().profiles().get(currentUser(request).id());
        sendJson(response, HttpServletResponse.SC_OK, ProfileResponse.from(user));
    }

    private void updateProfile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long userId = currentUser(request).id();
        UpdateProfileRequest body = readJson(request, UpdateProfileRequest.class);
        User updated = app().profiles().update(userId,
                body.firstName(), body.lastName(), body.phoneNumber(), body.dateOfBirth());
        sendJson(response, HttpServletResponse.SC_OK, ProfileResponse.from(updated));
    }

    /** Po zmianie hasła wszystkie sesje wygasają; bieżąca dostaje od razu nowe tokeny. */
    private void changePassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long userId = currentUser(request).id();
        ChangePasswordRequest body = readJson(request, ChangePasswordRequest.class);
        AuthenticatedUser user = app().auth().changePassword(userId, body.currentPassword(), body.newPassword());
        app().refreshTokens().revokeAll(user.id());
        sendTokens(request, response, user, app().refreshTokens().issue(user.id()));
    }

    private static ApiException notFound() {
        return new ApiException(HttpServletResponse.SC_NOT_FOUND, "Nie znaleziono zasobu");
    }
}
