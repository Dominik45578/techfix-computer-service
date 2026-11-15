package pk.wieik.ti.model;

public record Book(long id, long autorId, String tytulPl, String tytulEn,
                   int rokWydania, long wydawcaId) {
    public Book {
        Checks.notBlank(tytulPl, "tytul_pl");
        Checks.notBlank(tytulEn, "tytul_en");
        Checks.nonNegative(rokWydania, "rok_wydania");
    }

    public static Book of(long autorId, String tytulPl, String tytulEn,
                          int rokWydania, long wydawcaId) {
        return new Book(0, autorId, tytulPl, tytulEn, rokWydania, wydawcaId);
    }
}
