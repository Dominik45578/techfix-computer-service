package pk.wieik.ti.db;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Jdbc {
    private final DataSource dataSource;

    public Jdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> List<T> query(String sql, RowMapper<T> mapper, Object... params) throws SQLException {
        return execute(sql, Statement.NO_GENERATED_KEYS, params, ps -> {
            List<T> rows = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(mapper.map(rs));
                }
            }
            return rows;
        });
    }

    public <T> Optional<T> queryOne(String sql, RowMapper<T> mapper, Object... params) throws SQLException {
        List<T> rows = query(sql, mapper, params);
        if (rows.size() > 1) {
            throw new IllegalStateException("Oczekiwano najwyżej jednego wiersza, otrzymano " + rows.size());
        }
        return rows.stream().findFirst();
    }

    public long queryLong(String sql, Object... params) throws SQLException {
        return queryOne(sql, rs -> rs.getLong(1), params).orElse(0L);
    }

    public int update(String sql, Object... params) throws SQLException {
        return execute(sql, Statement.NO_GENERATED_KEYS, params, PreparedStatement::executeUpdate);
    }

    public long insert(String sql, Object... params) throws SQLException {
        return execute(sql, Statement.RETURN_GENERATED_KEYS, params, ps -> {
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException("Baza nie zwróciła wygenerowanego id");
                }
                return keys.getLong(1);
            }
        });
    }

    private <R> R execute(String sql, int generatedKeys, Object[] params,
                          StatementWork<R> work) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, generatedKeys)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return work.apply(ps);
        }
    }

    @FunctionalInterface
    private interface StatementWork<R> {
        R apply(PreparedStatement ps) throws SQLException;
    }
}
