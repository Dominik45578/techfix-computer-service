package pk.wieik.ti.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** {@code GET /api/me}: dane własnego konta. Dostęp pilnuje {@code RoleFilter} na {@code /api/me/*}. */
@WebServlet("/api/me")
public class MeServlet extends JsonServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO: pełne dane konta z UserRepository (telefon, data urodzenia) zamiast samego tokenu
        sendJson(response, HttpServletResponse.SC_OK, UserResponse.from(currentUser(request)));
    }
}
