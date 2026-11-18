package pk.wieik.ti.repository;

import pk.wieik.ti.model.User;
import pk.wieik.ti.model.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User> {

    /** Porównanie e-maila bez rozróżniania wielkości liter. */
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(UserRole role);

    /** {@link #update} nie zmienia hasła, robi się to tylko tutaj. */
    boolean updatePassword(long userId, String passwordHash);
}
