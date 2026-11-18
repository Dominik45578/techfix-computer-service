package pk.wieik.ti.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
interface RowMapper<T> {
    T map(ResultSet rs) throws SQLException;
}
