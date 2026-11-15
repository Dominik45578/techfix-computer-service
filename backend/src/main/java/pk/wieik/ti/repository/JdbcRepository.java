package pk.wieik.ti.repository;

import pk.wieik.ti.db.Jdbc;
import pk.wieik.ti.db.RowMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class JdbcRepository<T> {
    protected final Jdbc jdbc;
    private final String table;
    private final RowMapper<T> mapper;

    protected JdbcRepository(Jdbc jdbc, String table, RowMapper<T> mapper) {
        this.jdbc = jdbc;
        this.table = table;
        this.mapper = mapper;
    }

    public List<T> findAll() throws SQLException {
        return jdbc.query("SELECT * FROM " + table + " ORDER BY id", mapper);
    }

    public Optional<T> findById(long id) throws SQLException {
        return jdbc.queryOne("SELECT * FROM " + table + " WHERE id = ?", mapper, id);
    }

    public long count() throws SQLException {
        return jdbc.queryLong("SELECT COUNT(*) FROM " + table);
    }

    public boolean deleteById(long id) throws SQLException {
        return jdbc.update("DELETE FROM " + table + " WHERE id = ?", id) == 1;
    }

    protected Optional<T> findOneWhere(String condition, Object... params) throws SQLException {
        return jdbc.queryOne("SELECT * FROM " + table + " WHERE " + condition, mapper, params);
    }
}
