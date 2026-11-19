package pk.wieik.ti.repository.jdbc;

import pk.wieik.ti.repository.DeviceCategoryRepository;
import pk.wieik.ti.repository.PurchaseRepository;
import pk.wieik.ti.repository.RefreshTokenRepository;
import pk.wieik.ti.repository.ReservationRepository;
import pk.wieik.ti.repository.ServiceKindRepository;
import pk.wieik.ti.repository.ServiceOfferRepository;
import pk.wieik.ti.repository.TransactionRunner;
import pk.wieik.ti.repository.UserRepository;

import javax.sql.DataSource;

/**
 * Jedyna publiczna klasa pakietu {@code repository.jdbc}: tworzy implementacje JDBC
 * i oddaje je jako interfejsy z {@code repository}. Reszta pakietu jest package-private,
 * więc poza nim nie da się sięgnąć po {@code Jdbc} ani {@code Jdbc*Repository}.
 */
public final class JdbcRepositories {
    private final Jdbc jdbc;

    private final UserRepository users;
    private final RefreshTokenRepository refreshTokens;
    private final DeviceCategoryRepository deviceCategories;
    private final ServiceKindRepository serviceKinds;
    private final ServiceOfferRepository serviceOffers;
    private final PurchaseRepository purchases;
    private final ReservationRepository reservations;

    public JdbcRepositories(DataSource dataSource) {
        this.jdbc = new Jdbc(dataSource);
        this.users = new JdbcUserRepository(jdbc);
        this.refreshTokens = new JdbcRefreshTokenRepository(jdbc);
        this.deviceCategories = new JdbcDeviceCategoryRepository(jdbc);
        this.serviceKinds = new JdbcServiceKindRepository(jdbc);
        this.serviceOffers = new JdbcServiceOfferRepository(jdbc);
        this.purchases = new JdbcPurchaseRepository(jdbc);
        this.reservations = new JdbcReservationRepository(jdbc);
    }

    public TransactionRunner transactions() {
        return jdbc;
    }

    public UserRepository users() {
        return users;
    }

    public RefreshTokenRepository refreshTokens() {
        return refreshTokens;
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
}
