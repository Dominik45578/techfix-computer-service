package pk.wieik.ti.persistence.jdbc;

import pk.wieik.ti.domain.port.DataAccessException;
import pk.wieik.ti.domain.port.TransactionRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Cienka nakładka na JDBC: pobiera połączenie z puli, binduje parametry, mapuje wiersze
 * i tłumaczy {@link SQLException} na {@link DataAccessException}.
 * <p>
 * Wewnątrz {@link #inTransaction} wszystkie zapytania z bieżącego wątku idą
 * tym samym połączeniem (trzymanym w {@link ThreadLocal}).
 */
public final class Jdbc implements TransactionRunner {
    private final DataSource dataSource;
    private final ThreadLocal<Connection> currentTransaction = new ThreadLocal<>();

    public Jdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> List<T> query(String sql, RowMapper<T> mapper, Object... params) {
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

    public <T> Optional<T> queryOne(String sql, RowMapper<T> mapper, Object... params) {
        List<T> rows = query(sql, mapper, params);
        if (rows.size() > 1) {
            throw new IllegalStateException("Oczekiwano najwyżej jednego wiersza, otrzymano " + rows.size());
        }
        return rows.stream().findFirst();
    }

    public long queryLong(String sql, Object... params) {
        return queryOne(sql, rs -> rs.getLong(1), params).orElse(0L);
    }

    /** @return liczba zmienionych wierszy */
    public int update(String sql, Object... params) {
        return execute(sql, Statement.NO_GENERATED_KEYS, params, PreparedStatement::executeUpdate);
    }

    /** @return id nadane przez bazę */
    public long insert(String sql, Object... params) {
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

    @Override
    public <T> T inTransaction(Supplier<T> work) {
        if (currentTransaction.get() != null) {
            return work.get();
        }
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            currentTransaction.set(connection);
            try {
                T result = work.get();
                connection.commit();
                return result;
            } catch (RuntimeException | Error ex) {
                rollback(connection, ex);
                throw ex;
            } finally {
                currentTransaction.remove();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Błąd obsługi transakcji", ex);
        }
    }

    private <R> R execute(String sql, int generatedKeys, Object[] params, StatementWork<R> work) {
        try {
            Connection transaction = currentTransaction.get();
            if (transaction != null) {
                return run(transaction, sql, generatedKeys, params, work);
            }
            try (Connection connection = dataSource.getConnection()) {
                return run(connection, sql, generatedKeys, params, work);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Błąd zapytania SQL: " + ex.getMessage(), ex);
        }
    }

    private static <R> R run(Connection connection, String sql, int generatedKeys, Object[] params,
                             StatementWork<R> work) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(sql, generatedKeys)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return work.apply(ps);
        }
    }

    private static void rollback(Connection connection, Throwable cause) {
        try {
            connection.rollback();
        } catch (SQLException rollbackFailure) {
            cause.addSuppressed(rollbackFailure);
        }
    }

    @FunctionalInterface
    private interface StatementWork<R> {
        R apply(PreparedStatement ps) throws SQLException;
    }
}
