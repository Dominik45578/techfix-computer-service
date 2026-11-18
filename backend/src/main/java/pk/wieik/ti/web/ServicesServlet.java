package pk.wieik.ti.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pk.wieik.ti.model.DeviceCategory;
import pk.wieik.ti.model.Service;
import pk.wieik.ti.model.ServiceKind;

import java.io.IOException;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/uslugi")
public class ServicesServlet extends HttpServlet {
    private AppContext app;

    @Override
    public void init() {
        app = AppContext.from(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/logowanie");
            return;
        }
        try {
            Map<Long, String> categories = new HashMap<>();
            for (DeviceCategory c : app.deviceCategories().findAll()) {
                categories.put(c.id(), c.deviceName());
            }
            Map<Long, String> kinds = new HashMap<>();
            for (ServiceKind k : app.serviceKinds().findAll()) {
                kinds.put(k.id(), k.kindName());
            }

            StringBuilder html = new StringBuilder();
            html.append("<h1>Katalog usług</h1><p>Zalogowano jako: <strong>")
                .append(Html.escape(String.valueOf(session.getAttribute("fullName"))))
                .append("</strong> (rola: ")
                .append(Html.escape(String.valueOf(session.getAttribute("role"))))
                .append(")</p><table><thead><tr><th>Urządzenie</th><th>Usługa</th><th>Rodzaj</th>")
                .append("<th>Opis</th><th>Cena</th><th>Czas (min)</th>")
                .append("</tr></thead><tbody>");

            List<Service> rows = app.services().findActive();
            for (Service s : rows) {
                html.append("<tr><td>").append(Html.escape(categories.getOrDefault(s.deviceCategoryId(), "")))
                    .append("</td><td>").append(Html.escape(s.serviceName()))
                    .append("</td><td>").append(Html.escape(kinds.getOrDefault(s.serviceKindId(), "")))
                    .append("</td><td>").append(Html.escape(s.description()))
                    .append("</td><td>").append(s.baseCost().setScale(2, RoundingMode.HALF_UP))
                    .append(" zł</td><td>").append(s.estimatedDurationMinutes())
                    .append("</td></tr>");
            }
            html.append("</tbody></table>");
            if (rows.isEmpty()) {
                html.append("<p>Brak aktywnych usług. Uruchom aplikację ze zmienną DANE_TESTOWE=true.</p>");
            }
            html.append("<form method='post' action='")
                .append(Html.escape(request.getContextPath()))
                .append("/wyloguj'><button>Wyloguj</button></form>");
            Html.send(response, "Katalog usług", html.toString());
        } catch (SQLException ex) {
            throw new ServletException("Błąd odczytu usług", ex);
        }
    }
}
