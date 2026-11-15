package pk.wieik.ti.auth;

import pk.wieik.ti.model.User;

public record AuthenticatedUser(long id, String login, int uprawnienia) {
    static AuthenticatedUser from(User user) {
        return new AuthenticatedUser(user.id(), user.login(), user.uprawnienia());
    }
}
