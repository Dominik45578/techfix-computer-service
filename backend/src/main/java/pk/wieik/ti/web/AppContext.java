package pk.wieik.ti.web;

import jakarta.servlet.ServletContext;
import pk.wieik.ti.auth.AuthService;
import pk.wieik.ti.db.Jdbc;
import pk.wieik.ti.repository.AuthorRepository;
import pk.wieik.ti.repository.BookRepository;
import pk.wieik.ti.repository.PublisherRepository;
import pk.wieik.ti.repository.UserRepository;

public record AppContext(AuthService auth,
                         AuthorRepository authors,
                         PublisherRepository publishers,
                         BookRepository books) {
    private static final String ATTRIBUTE = AppContext.class.getName();

    public static AppContext create(Jdbc jdbc) {
        return new AppContext(
                new AuthService(new UserRepository(jdbc)),
                new AuthorRepository(jdbc),
                new PublisherRepository(jdbc),
                new BookRepository(jdbc));
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
