package pk.wieik.ti.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Sprawdzenie, czy aplikacja wstała. Używane przez HEALTHCHECK w Dockerfile; nie wymaga autoryzacji. */
@WebServlet("/api/health")
public class HealthServlet extends JsonServlet {

    public record HealthStatus(String status) {}

    private static final HealthStatus UP = new HealthStatus("UP");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        sendJson(response, HttpServletResponse.SC_OK, UP);
    }
}
