package pk.wieik.ti.domain.port;

import java.util.function.Supplier;

/**
 * Port transakcji: wszystkie wywołania repozytoriów wewnątrz {@code work}
 * idą jednym połączeniem i są zatwierdzane albo wycofywane razem.
 */
public interface TransactionRunner {

    /** Zatwierdza po poprawnym zakończeniu, wycofuje przy każdym wyjątku. Zagnieżdżenie dołącza do bieżącej. */
    <T> T inTransaction(Supplier<T> work);

    default void runInTransaction(Runnable work) {
        inTransaction(() -> {
            work.run();
            return null;
        });
    }
}
