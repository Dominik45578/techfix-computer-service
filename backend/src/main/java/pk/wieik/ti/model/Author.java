package pk.wieik.ti.model;

public record Author(long id, String imie, String nazwisko) {
    public Author {
        Checks.notBlank(imie, "imie");
        Checks.notBlank(nazwisko, "nazwisko");
    }

    public static Author of(String imie, String nazwisko) {
        return new Author(0, imie, nazwisko);
    }

    public String pelneImie() {
        return imie + " " + nazwisko;
    }
}
