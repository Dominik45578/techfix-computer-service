package pk.wieik.ti.db;

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

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("""
                CREATE TABLE IF NOT EXISTS uzytkownicy (
                    id INTEGER PRIMARY KEY,
                    login TEXT NOT NULL UNIQUE COLLATE NOCASE,
                    haslo TEXT NOT NULL,
                    uprawnienia INTEGER NOT NULL DEFAULT 0
                        CHECK (uprawnienia IN (0, 1, 2))
                )
                """);

            statement.execute("""
                CREATE TABLE IF NOT EXISTS autorzy (
                    id INTEGER PRIMARY KEY,
                    imie TEXT NOT NULL,
                    nazwisko TEXT NOT NULL,
                    UNIQUE (imie, nazwisko)
                )
                """);

            statement.execute("""
                CREATE TABLE IF NOT EXISTS wydawcy (
                    id INTEGER PRIMARY KEY,
                    nazwa TEXT NOT NULL UNIQUE
                )
                """);

            statement.execute("""
                CREATE TABLE IF NOT EXISTS ksiazki (
                    id INTEGER PRIMARY KEY,
                    autor_id INTEGER NOT NULL,
                    tytul_pl TEXT NOT NULL,
                    tytul_en TEXT NOT NULL,
                    rok_wydania INTEGER NOT NULL CHECK (rok_wydania >= 0),
                    wydawca_id INTEGER NOT NULL,
                    FOREIGN KEY (autor_id) REFERENCES autorzy(id)
                        ON UPDATE CASCADE ON DELETE RESTRICT,
                    FOREIGN KEY (wydawca_id) REFERENCES wydawcy(id)
                        ON UPDATE CASCADE ON DELETE RESTRICT
                )
                """);

            statement.execute("CREATE INDEX IF NOT EXISTS idx_ksiazki_autor ON ksiazki(autor_id)");
            statement.execute("CREATE INDEX IF NOT EXISTS idx_ksiazki_wydawca ON ksiazki(wydawca_id)");
        }
    }
}
