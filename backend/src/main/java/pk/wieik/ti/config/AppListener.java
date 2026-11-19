package pk.wieik.ti.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import pk.wieik.ti.model.UserRole;
import pk.wieik.ti.repository.jdbc.JdbcRepositories;
import pk.wieik.ti.security.AccessTokenVerifier;
import pk.wieik.ti.security.AuthenticationFilter;
import pk.wieik.ti.security.RoleFilter;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

/** Start aplikacji: baza → repozytoria → serwisy → {@link AppContext}. */
@WebListener
public class AppListener implements ServletContextListener {
    private Database database;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        try {
            database = Database.open(Database.defaultFile(), isEnabled("DANE_TESTOWE"));
            servletContext.log("SQLite: " + database.file());
            System.out.println("SQLite: " + database.file());

            AppContext context = AppContext.create(new JdbcRepositories(database.dataSource()));
            context.register(servletContext);
            createFirstAdminIfConfigured(context, servletContext);

            // TODO krok 2: tokenService::verify (JWT); na razie każdy token jest odrzucany
            registerSecurityFilters(servletContext, token -> Optional.empty());
        } catch (Exception e) {
            contextDestroyed(event);
            throw new IllegalStateException("Nie można zainicjalizować bazy SQLite", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        if (database != null) {
            database.close();
            database = null;
        }
        deregisterJdbcDrivers(event.getServletContext());
    }

    /**
     * Kolejność rejestracji = kolejność w łańcuchu: najpierw ustalenie użytkownika, potem kontrola ról.
     * Wzorzec {@code /x/*} obejmuje też samo {@code /x}. Domyślnie tylko {@code DispatcherType.REQUEST},
     * więc przekierowanie do {@code /error} nie przechodzi przez filtry drugi raz.
     */
    private static void registerSecurityFilters(ServletContext servletContext, AccessTokenVerifier tokens) {
        servletContext.addFilter("authentication", new AuthenticationFilter(tokens))
                .addMappingForUrlPatterns(null, true, "/api/*");
        servletContext.addFilter("requireUser", RoleFilter.authenticated())
                .addMappingForUrlPatterns(null, true, "/api/me/*");
        servletContext.addFilter("requireAdmin", RoleFilter.requireRole(UserRole.ADMIN))
                .addMappingForUrlPatterns(null, true, "/api/admin/*");
    }

    private static void deregisterJdbcDrivers(ServletContext servletContext) {
        ClassLoader appClassLoader = AppListener.class.getClassLoader();
        for (Driver driver : Collections.list(DriverManager.getDrivers())) {
            if (driver.getClass().getClassLoader() != appClassLoader) {
                continue;
            }
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException ex) {
                servletContext.log("Nie można wyrejestrować sterownika " + driver.getClass().getName(), ex);
            }
        }
    }

    private static void createFirstAdminIfConfigured(AppContext context, ServletContext servletContext)
            throws Exception {
        String email = System.getenv("ADMIN_EMAIL");
        String password = System.getenv("ADMIN_PASSWORD");
        if ((email == null) != (password == null)) {
            throw new IllegalStateException("Ustaw oba parametry ADMIN_EMAIL i ADMIN_PASSWORD");
        }
        if (email != null && context.auth().createFirstAdmin(email, password)) {
            servletContext.log("Utworzono pierwsze konto administratora");
        }
    }

    private static boolean isEnabled(String envVariable) {
        return "true".equalsIgnoreCase(System.getenv(envVariable));
    }
}
