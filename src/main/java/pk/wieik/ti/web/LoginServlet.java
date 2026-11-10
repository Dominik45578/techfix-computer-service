package pk.wieik.ti.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pk.wieik.ti.repository.UserRepository;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/logowanie")
public class LoginServlet extends HttpServlet {
    private final UserRepository users = new UserRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String path = Html.escape(request.getContextPath());
        String error = request.getParameter("blad") != null
                ? "<p class='error'>Nieprawidłowy login lub hasło.</p>" : "";
        Html.send(response, "Logowanie", """
            <h1>Biblioteka – logowanie</h1>
            %s
            <form method="post" action="%s/logowanie">
                <label>Login: <input name="login" required maxlength="64" autocomplete="username"></label><br>
                <label>Hasło: <input type="password" name="haslo" required autocomplete="current-password"></label><br>
                <button type="submit">Zaloguj</button>
            </form>
            """.formatted(error, path));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        try {
            var user = users.authenticate(request.getParameter("login"),
                    request.getParameter("haslo"));
            if (user.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/logowanie?blad=1");
                return;
            }

            // Usunięcie poprzedniej sesji zapobiega utrwaleniu jej identyfikatora.
            HttpSession previous = request.getSession(false);
            if (previous != null) previous.invalidate();
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.get().id());
            session.setAttribute("login", user.get().login());
            session.setAttribute("uprawnienia", user.get().uprawnienia());
            session.setMaxInactiveInterval(20 * 60);
            response.sendRedirect(request.getContextPath() + "/ksiazki");
        } catch (SQLException ex) {
            throw new ServletException("Błąd bazy podczas logowania", ex);
        }
    }
}
