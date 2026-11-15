package pk.wieik.ti.model;

public record User(long id, String login, String hasloHash, int uprawnienia) {
    public static final int ADMINISTRATOR = 2;

    public User {
        Checks.notBlank(login, "login");
        Checks.notBlank(hasloHash, "haslo");
        Checks.inRange(uprawnienia, 0, ADMINISTRATOR, "uprawnienia");
    }

    public static User of(String login, String hasloHash, int uprawnienia) {
        return new User(0, login, hasloHash, uprawnienia);
    }

    @Override
    public String toString() {
        return "User[id=" + id + ", login=" + login + ", uprawnienia=" + uprawnienia + "]";
    }
}
