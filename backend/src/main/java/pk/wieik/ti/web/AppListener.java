package pk.wieik.ti.web;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import pk.wieik.ti.persistence.Persistence;
import pk.wieik.ti.persistence.database.Database;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;

@WebListener
public class AppListener implements ServletContextListener {
    private Persistence persistence;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        try {
            persistence = Persistence.open(Database.defaultFile(), isEnabled("DANE_TESTOWE"));
            servletContext.log("SQLite: " + persistence.file());
            System.out.println("SQLite: " + persistence.file());

            AppContext context = AppContext.create(persistence);
            context.register(servletContext);
            createFirstAdminIfConfigured(context, servletContext);
        } catch (Exception e) {
            contextDestroyed(event);
            throw new IllegalStateException("Nie można zainicjalizować bazy SQLite", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        if (persistence != null) {
            persistence.close();
            persistence = null;
        }
        deregisterJdbcDrivers(event.getServletContext());
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
