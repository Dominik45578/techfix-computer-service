package pk.wieik.ti.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

final class Database implements AutoCloseable {
    private static final String SCHEMA_LOCATION = "classpath:db/migration";
    private static final String SAMPLE_DATA_LOCATION = "classpath:db/dane-testowe";

    private final Path file;
    private final HikariDataSource dataSource;

    private Database(Path file, HikariDataSource dataSource) {
        this.file = file;
        this.dataSource = dataSource;
    }

    public static Path defaultFile() {
        return Path.of(
                System.getProperty("catalina.base", System.getProperty("user.home")),
                "data", "serwis", "serwis.db"
        ).toAbsolutePath().normalize();
    }

    public static Database open(Path file, boolean withSampleData) throws IOException {
        Files.createDirectories(file.getParent());
        HikariDataSource dataSource = new HikariDataSource(poolConfig(file));
        try {
            migrate(dataSource, withSampleData);
        } catch (RuntimeException ex) {
            dataSource.close();
            throw ex;
        }
        return new Database(file, dataSource);
    }

    public Path file() {
        return file;
    }

    public DataSource dataSource() {
        return dataSource;
    }

    @Override
    public void close() {
        dataSource.close();
    }

    private static HikariConfig poolConfig(Path file) {
        HikariConfig config = new HikariConfig();
        config.setPoolName("serwis-sqlite");
        config.setDriverClassName("org.sqlite.JDBC");
        config.setJdbcUrl("jdbc:sqlite:" + file);
        config.setMaximumPoolSize(4);
        config.addDataSourceProperty("foreign_keys", "true");
        config.addDataSourceProperty("busy_timeout", "5000");
        return config;
    }

    private static void migrate(DataSource dataSource, boolean withSampleData) {
        String[] locations = withSampleData
                ? new String[] {SCHEMA_LOCATION, SAMPLE_DATA_LOCATION}
                : new String[] {SCHEMA_LOCATION};

        Flyway.configure()
                .dataSource(dataSource)
                .locations(locations)
                .baselineOnMigrate(true)
                .baselineVersion("1")
                .ignoreMigrationPatterns("*:future", "repeatable:missing")
                .load()
                .migrate();
    }
}
