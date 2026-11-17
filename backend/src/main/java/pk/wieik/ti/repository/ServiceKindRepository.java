package pk.wieik.ti.repository;

import pk.wieik.ti.db.Jdbc;
import pk.wieik.ti.model.ServiceKind;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ServiceKindRepository extends JdbcRepository<ServiceKind> {
    public ServiceKindRepository(Jdbc jdbc) {
        super(jdbc, "service_kind", ServiceKindRepository::map);
    }

    public List<ServiceKind> findAllByName() throws SQLException {
        return jdbc.query("SELECT * FROM service_kind ORDER BY kind_name",
                ServiceKindRepository::map);
    }

    public ServiceKind insert(ServiceKind kind) throws SQLException {
        long id = jdbc.insert(
                "INSERT INTO service_kind (kind_name, description) VALUES (?, ?)",
                kind.kindName(), kind.description());
        return new ServiceKind(id, kind.kindName(), kind.description());
    }

    public boolean update(ServiceKind kind) throws SQLException {
        return jdbc.update(
                "UPDATE service_kind SET kind_name = ?, description = ? WHERE id = ?",
                kind.kindName(), kind.description(), kind.id()) == 1;
    }

    private static ServiceKind map(ResultSet rs) throws SQLException {
        return new ServiceKind(
                rs.getLong("id"),
                rs.getString("kind_name"),
                rs.getString("description"));
    }
}
