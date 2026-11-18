package pk.wieik.ti.config;

import jakarta.servlet.ServletContext;
import pk.wieik.ti.repository.DeviceCategoryRepository;
import pk.wieik.ti.repository.PurchaseRepository;
import pk.wieik.ti.repository.ReservationRepository;
import pk.wieik.ti.repository.ServiceKindRepository;
import pk.wieik.ti.repository.ServiceOfferRepository;
import pk.wieik.ti.repository.TransactionRunner;
import pk.wieik.ti.repository.UserRepository;
import pk.wieik.ti.repository.jdbc.JdbcRepositories;
import pk.wieik.ti.service.AuthService;

/**
 * Zależności dostępne dla kontrolerów (odpowiednik kontenera beanów).
 * Składane raz w {@link AppListener}; servlety pobierają je przez {@link #from}.
 */
public record AppContext(AuthService auth,
                         TransactionRunner transactions,
                         UserRepository users,
                         DeviceCategoryRepository deviceCategories,
                         ServiceKindRepository serviceKinds,
                         ServiceOfferRepository serviceOffers,
                         PurchaseRepository purchases,
                         ReservationRepository reservations) {
    private static final String ATTRIBUTE = AppContext.class.getName();

    static AppContext create(JdbcRepositories repositories) {
        return new AppContext(
                new AuthService(repositories.users()),
                repositories.transactions(),
                repositories.users(),
                repositories.deviceCategories(),
                repositories.serviceKinds(),
                repositories.serviceOffers(),
                repositories.purchases(),
                repositories.reservations());
    }

    public static AppContext from(ServletContext servletContext) {
        Object context = servletContext.getAttribute(ATTRIBUTE);
        if (context == null) {
            throw new IllegalStateException("AppContext nie został zainicjalizowany przez AppListener");
        }
        return (AppContext) context;
    }

    void register(ServletContext servletContext) {
        servletContext.setAttribute(ATTRIBUTE, this);
    }
}
