package pk.wieik.ti.web;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import pk.wieik.ti.db.Database;
import pk.wieik.ti.db.Jdbc;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;

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

            AppContext context = AppContext.create(new Jdbc(database.dataSource()));
            context.register(servletContext);
            createFirstAdminIfConfigured(context, servletContext);
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
        String login = System.getenv("ADMIN_LOGIN");
        String password = System.getenv("ADMIN_PASSWORD");
        if ((login == null) != (password == null)) {
            throw new IllegalStateException("Ustaw oba parametry ADMIN_LOGIN i ADMIN_PASSWORD");
        }
        if (login != null && context.auth().createFirstAdmin(login, password)) {
            servletContext.log("Utworzono pierwsze konto administratora");
        }
    }

    private static boolean isEnabled(String envVariable) {
        return "true".equalsIgnoreCase(System.getenv(envVariable));
    }
}
