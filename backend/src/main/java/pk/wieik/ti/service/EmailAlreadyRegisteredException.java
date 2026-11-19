package pk.wieik.ti.service;

public class EmailAlreadyRegisteredException extends RuntimeException {
    EmailAlreadyRegisteredException() {
        super("Konto z takim adresem e-mail już istnieje");
    }

    EmailAlreadyRegisteredException(Throwable cause) {
        super("Konto z takim adresem e-mail już istnieje", cause);
    }
}
