package pk.wieik.ti.db;

import org.flywaydb.core.Flyway;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/** Jedno miejsce odpowiedzialne za konfigurację SQLite i schemat bazy. */
public final class Database {
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final Path DB_FILE = Path.of(
            System.getProperty("catalina.base", System.getProperty("user.home")),
            "data", "biblioteka", "biblioteka.db"
    ).toAbsolutePath().normalize();

    private Database() {}

    public static Path getDatabaseFile() {
        return DB_FILE;
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("No JDBC driver");
            e.printStackTrace();
        }

        // Przy pierwszym połączeniu SQLite utworzy brakujący plik bazy.
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + DB_FILE);
        try (Statement statement = connection.createStatement()) {
            // PRAGMA foreign_keys obowiązuje osobno dla KAŻDEGO połączenia.
            statement.execute("PRAGMA foreign_keys = ON");
            statement.execute("PRAGMA busy_timeout = 5000");
        } catch (SQLException ex) {
            connection.close();
            throw ex;
        }
        return connection;
    }

    public static void initialize() throws IOException, SQLException {
        Files.createDirectories(DB_FILE.getParent());

        // Schemat bazy jest zarządzany przez Flyway.
        // Skrypty: src/main/resources/db/migration/V<wersja>__<opis>.sql
        Flyway.configure()
                .dataSource("jdbc:sqlite:" + DB_FILE, null, null)
                .locations("classpath:db/migration")
                // Baza utworzona wcześniej przez CREATE TABLE IF NOT EXISTS (bez historii Flyway)
                // zostaje oznaczona jako wersja 1, zamiast ponownie tworzyć istniejące tabele.
                .baselineOnMigrate(true)
                .baselineVersion("1")
                .load()
                .migrate();
    }
}
