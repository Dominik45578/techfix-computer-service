package pk.wieik.ti.persistence;

import pk.wieik.ti.domain.port.TransactionRunner;
import pk.wieik.ti.domain.port.repository.DeviceCategoryRepository;
import pk.wieik.ti.domain.port.repository.PurchaseRepository;
import pk.wieik.ti.domain.port.repository.ReservationRepository;
import pk.wieik.ti.domain.port.repository.ServiceKindRepository;
import pk.wieik.ti.domain.port.repository.ServiceOfferRepository;
import pk.wieik.ti.domain.port.repository.UserRepository;
import pk.wieik.ti.persistence.database.Database;
import pk.wieik.ti.persistence.jdbc.Jdbc;
import pk.wieik.ti.persistence.repository.JdbcDeviceCategoryRepository;
import pk.wieik.ti.persistence.repository.JdbcPurchaseRepository;
import pk.wieik.ti.persistence.repository.JdbcReservationRepository;
import pk.wieik.ti.persistence.repository.JdbcServiceKindRepository;
import pk.wieik.ti.persistence.repository.JdbcServiceOfferRepository;
import pk.wieik.ti.persistence.repository.JdbcUserRepository;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Adapter bazy danych: jedyne miejsce, które wie o SQLite, puli połączeń i JDBC.
 * Na zewnątrz wystawia wyłącznie porty z {@code domain.port}.
 */
public final class Persistence implements AutoCloseable {
    private final Database database;
    private final Jdbc jdbc;

    private final UserRepository users;
    private final DeviceCategoryRepository deviceCategories;
    private final ServiceKindRepository serviceKinds;
    private final ServiceOfferRepository serviceOffers;
    private final PurchaseRepository purchases;
    private final ReservationRepository reservations;

    private Persistence(Database database) {
        this.database = database;
        this.jdbc = new Jdbc(database.dataSource());
        this.users = new JdbcUserRepository(jdbc);
        this.deviceCategories = new JdbcDeviceCategoryRepository(jdbc);
        this.serviceKinds = new JdbcServiceKindRepository(jdbc);
        this.serviceOffers = new JdbcServiceOfferRepository(jdbc);
        this.purchases = new JdbcPurchaseRepository(jdbc);
        this.reservations = new JdbcReservationRepository(jdbc);
    }

    /** Otwiera plik bazy, uruchamia migracje Flyway (opcjonalnie z danymi testowymi). */
    public static Persistence open(Path file, boolean withSampleData) throws IOException {
        return new Persistence(Database.open(file, withSampleData));
    }

    public Path file() {
        return database.file();
    }

    public TransactionRunner transactions() {
        return jdbc;
    }

    public UserRepository users() {
        return users;
    }

    public DeviceCategoryRepository deviceCategories() {
        return deviceCategories;
    }

    public ServiceKindRepository serviceKinds() {
        return serviceKinds;
    }

    public ServiceOfferRepository serviceOffers() {
        return serviceOffers;
    }

    public PurchaseRepository purchases() {
        return purchases;
    }

    public ReservationRepository reservations() {
        return reservations;
    }

    @Override
    public void close() {
        database.close();
    }
}
