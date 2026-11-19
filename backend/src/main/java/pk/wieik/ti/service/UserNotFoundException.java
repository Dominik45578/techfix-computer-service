package pk.wieik.ti.service;

public class UserNotFoundException extends RuntimeException {
    UserNotFoundException() {
        super("Nie znaleziono użytkownika");
    }
}
