package pk.wieik.ti.repository;

import java.util.List;
import java.util.Optional;

/**
 * Wspólny kontrakt repozytorium encji z kluczem {@code id}.
 * Implementacje mogą zgłosić {@link pk.wieik.ti.repository.DataAccessException}.
 */
public interface CrudRepository<T> {

    List<T> findAll();

    Optional<T> findById(long id);

    long count();

    /** Zapisuje nową encję i zwraca ją z nadanym przez bazę id. */
    T insert(T entity);

    /** @return {@code true}, jeśli rekord o tym id istniał i został zmieniony */
    boolean update(T entity);

    /** @return {@code true}, jeśli rekord o tym id istniał i został usunięty */
    boolean deleteById(long id);
}
