package pk.wieik.ti.web;

import jakarta.servlet.ServletContext;
import pk.wieik.ti.auth.AuthService;
import pk.wieik.ti.domain.port.TransactionRunner;
import pk.wieik.ti.domain.port.repository.DeviceCategoryRepository;
import pk.wieik.ti.domain.port.repository.PurchaseRepository;
import pk.wieik.ti.domain.port.repository.ReservationRepository;
import pk.wieik.ti.domain.port.repository.ServiceKindRepository;
import pk.wieik.ti.domain.port.repository.ServiceOfferRepository;
import pk.wieik.ti.domain.port.repository.UserRepository;
import pk.wieik.ti.persistence.Persistence;

/** Zależności aplikacji widziane przez servlety: same porty, bez szczegółów JDBC. */
public record AppContext(AuthService auth,
                         TransactionRunner transactions,
                         UserRepository users,
                         DeviceCategoryRepository deviceCategories,
                         ServiceKindRepository serviceKinds,
                         ServiceOfferRepository serviceOffers,
                         PurchaseRepository purchases,
                         ReservationRepository reservations) {
    private static final String ATTRIBUTE = AppContext.class.getName();

    public static AppContext create(Persistence persistence) {
        return new AppContext(
                new AuthService(persistence.users()),
                persistence.transactions(),
                persistence.users(),
                persistence.deviceCategories(),
                persistence.serviceKinds(),
                persistence.serviceOffers(),
                persistence.purchases(),
                persistence.reservations());
    }

    public static AppContext from(ServletContext servletContext) {
        Object context = servletContext.getAttribute(ATTRIBUTE);
        if (context == null) {
            throw new IllegalStateException("AppContext nie został zainicjalizowany przez AppListener");
        }
        return (AppContext) context;
    }

    public void register(ServletContext servletContext) {
        servletContext.setAttribute(ATTRIBUTE, this);
    }
}
