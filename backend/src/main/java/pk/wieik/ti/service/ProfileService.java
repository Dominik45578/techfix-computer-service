package pk.wieik.ti.service;

import pk.wieik.ti.model.User;
import pk.wieik.ti.repository.UserRepository;

import java.time.LocalDate;

import static pk.wieik.ti.service.UserValidation.*;

/** Dane profilu zalogowanego użytkownika. E-mail i rola się tu nie zmieniają. */
public class ProfileService {
    private final UserRepository users;

    public ProfileService(UserRepository users) {
        this.users = users;
    }

    public User get(long userId) {
        return users.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public User update(long userId, String firstName, String lastName, String phoneNumber, LocalDate dateOfBirth) {
        User updated = get(userId).withProfile(
                normalizeName(firstName, "Imię"),
                normalizeName(lastName, "Nazwisko"),
                normalizePhone(phoneNumber),
                validateDateOfBirth(dateOfBirth));
        if (!users.update(updated)) {
            throw new UserNotFoundException();
        }
        return updated;
    }
}
