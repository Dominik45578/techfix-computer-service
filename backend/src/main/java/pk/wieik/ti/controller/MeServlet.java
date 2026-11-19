package pk.wieik.ti.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pk.wieik.ti.controller.dto.UserResponse;

import java.io.IOException;

@WebServlet("/api/me")
public class MeServlet extends JsonServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        sendJson(response, HttpServletResponse.SC_OK, UserResponse.from(currentUser(request)));
    }
}
