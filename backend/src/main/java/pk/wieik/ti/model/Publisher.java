package pk.wieik.ti.model;

public record Publisher(long id, String nazwa) {
    public Publisher {
        Checks.notBlank(nazwa, "nazwa");
    }

    public static Publisher of(String nazwa) {
        return new Publisher(0, nazwa);
    }
}
