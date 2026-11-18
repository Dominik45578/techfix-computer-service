package pk.wieik.ti.repository.jdbc;

import pk.wieik.ti.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Wspólna część adapterów JDBC: operacje po {@code id} i pomocnicze zapytania
 * {@code SELECT * FROM <tabela> WHERE ...}. Podklasy dopisują tylko insert/update i zapytania własne.
 */
abstract class JdbcRepository<T> implements CrudRepository<T> {
    protected final Jdbc jdbc;
    protected final RowMapper<T> mapper;
    private final String table;

    protected JdbcRepository(Jdbc jdbc, String table, RowMapper<T> mapper) {
        this.jdbc = jdbc;
        this.table = table;
        this.mapper = mapper;
    }

    @Override
    public List<T> findAll() {
        return jdbc.query("SELECT * FROM " + table + " ORDER BY id", mapper);
    }

    @Override
    public Optional<T> findById(long id) {
        return findOneWhere("id = ?", id);
    }

    @Override
    public long count() {
        return jdbc.queryLong("SELECT COUNT(*) FROM " + table);
    }

    @Override
    public boolean deleteById(long id) {
        return jdbc.update("DELETE FROM " + table + " WHERE id = ?", id) == 1;
    }

    /** @param conditionAndOrder fragment po {@code WHERE}, np. {@code "user_id = ? ORDER BY id"} */
    protected List<T> findWhere(String conditionAndOrder, Object... params) {
        return jdbc.query("SELECT * FROM " + table + " WHERE " + conditionAndOrder, mapper, params);
    }

    protected Optional<T> findOneWhere(String condition, Object... params) {
        return jdbc.queryOne("SELECT * FROM " + table + " WHERE " + condition, mapper, params);
    }

    protected long countWhere(String condition, Object... params) {
        return jdbc.queryLong("SELECT COUNT(*) FROM " + table + " WHERE " + condition, params);
    }
}
