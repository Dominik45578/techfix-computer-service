package pk.wieik.ti.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pk.wieik.ti.model.BookDetails;
import pk.wieik.ti.repository.BookRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/ksiazki")
public class BooksServlet extends HttpServlet {
    private BookRepository books;

    @Override
    public void init() {
        books = AppContext.from(getServletContext()).books();
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
            StringBuilder html = new StringBuilder();
            html.append("<h1>Spis książek</h1><p>Zalogowano jako: <strong>")
                .append(Html.escape((String) session.getAttribute("login")))
                .append("</strong> (uprawnienia: ")
                .append(session.getAttribute("uprawnienia"))
                .append(")</p><table><thead><tr><th>Autor</th><th>Tytuł PL</th>")
                .append("<th>Tytuł EN</th><th>Rok</th><th>Wydawca</th>")
                .append("</tr></thead><tbody>");

            List<BookDetails> rows = books.findAllDetails();
            for (BookDetails row : rows) {
                html.append("<tr><td>").append(Html.escape(row.autor()))
                    .append("</td><td>").append(Html.escape(row.tytulPl()))
                    .append("</td><td>").append(Html.escape(row.tytulEn()))
                    .append("</td><td>").append(row.rokWydania())
                    .append("</td><td>").append(Html.escape(row.wydawca()))
                    .append("</td></tr>");
            }
            html.append("</tbody></table>");
            if (rows.isEmpty()) {
                html.append("<p>Nie ma jeszcze książek. Uruchom aplikację ze zmienną DANE_TESTOWE=true.</p>");
            }
            html.append("<form method='post' action='")
                .append(Html.escape(request.getContextPath()))
                .append("/wyloguj'><button>Wyloguj</button></form>");
            Html.send(response, "Spis książek", html.toString());
        } catch (SQLException ex) {
            throw new ServletException("Błąd odczytu książek", ex);
        }
    }
}
