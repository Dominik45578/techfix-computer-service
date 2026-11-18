package pk.wieik.ti.web;

import jakarta.servlet.ServletContext;
import pk.wieik.ti.auth.AuthService;
import pk.wieik.ti.db.Jdbc;
import pk.wieik.ti.repository.DeviceCategoryRepository;
import pk.wieik.ti.repository.PurchaseRepository;
import pk.wieik.ti.repository.ReservationRepository;
import pk.wieik.ti.repository.ServiceKindRepository;
import pk.wieik.ti.repository.ServiceRepository;
import pk.wieik.ti.repository.UserRepository;

public record AppContext(AuthService auth,
                         UserRepository users,
                         DeviceCategoryRepository deviceCategories,
                         ServiceKindRepository serviceKinds,
                         ServiceRepository services,
                         PurchaseRepository purchases,
                         ReservationRepository reservations) {
    private static final String ATTRIBUTE = AppContext.class.getName();

    public static AppContext create(Jdbc jdbc) {
        UserRepository users = new UserRepository(jdbc);
        return new AppContext(
                new AuthService(users),
                users,
                new DeviceCategoryRepository(jdbc),
                new ServiceKindRepository(jdbc),
                new ServiceRepository(jdbc),
                new PurchaseRepository(jdbc),
                new ReservationRepository(jdbc));
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
