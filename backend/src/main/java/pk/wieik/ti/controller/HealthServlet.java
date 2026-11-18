package pk.wieik.ti.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Sprawdzenie, czy aplikacja wstała. Używane przez HEALTHCHECK w Dockerfile; nie wymaga autoryzacji. */
@WebServlet("/api/health")
public class HealthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Json.send(response, HttpServletResponse.SC_OK, "{\"status\":\"UP\"}");
    }
}
