package pk.wieik.ti.repository;

import pk.wieik.ti.db.Jdbc;
import pk.wieik.ti.model.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static pk.wieik.ti.db.SqlTypes.*;

public class ServiceRepository extends JdbcRepository<Service> {
    public ServiceRepository(Jdbc jdbc) {
        super(jdbc, "services", ServiceRepository::map);
    }

    public List<Service> findActive() throws SQLException {
        return jdbc.query("SELECT * FROM services WHERE is_active = 1 ORDER BY service_name",
                ServiceRepository::map);
    }

    public List<Service> findActiveByDeviceCategory(long deviceCategoryId) throws SQLException {
        return jdbc.query("""
                SELECT * FROM services
                WHERE is_active = 1 AND device_categorie_id = ?
                ORDER BY service_name
                """, ServiceRepository::map, deviceCategoryId);
    }

    public List<Service> findActiveByServiceKind(long serviceKindId) throws SQLException {
        return jdbc.query("""
                SELECT * FROM services
                WHERE is_active = 1 AND service_kind_id = ?
                ORDER BY service_name
                """, ServiceRepository::map, serviceKindId);
    }

    public Service insert(Service service) throws SQLException {
        long id = jdbc.insert("""
                INSERT INTO services (device_categorie_id, service_name, service_kind_id, description,
                                      base_cost, estimated_duration_minutes, is_active)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """,
                service.deviceCategoryId(), service.serviceName(), service.serviceKindId(),
                service.description(), toDb(service.baseCost()),
                service.estimatedDurationMinutes(), toDb(service.active()));
        return new Service(id, service.deviceCategoryId(), service.serviceName(),
                service.serviceKindId(), service.description(), service.baseCost(),
                service.estimatedDurationMinutes(), service.active());
    }

    public boolean update(Service service) throws SQLException {
        return jdbc.update("""
                UPDATE services
                SET device_categorie_id = ?, service_name = ?, service_kind_id = ?, description = ?,
                    base_cost = ?, estimated_duration_minutes = ?, is_active = ?
                WHERE id = ?
                """,
                service.deviceCategoryId(), service.serviceName(), service.serviceKindId(),
                service.description(), toDb(service.baseCost()),
                service.estimatedDurationMinutes(), toDb(service.active()),
                service.id()) == 1;
    }

    public boolean setActive(long id, boolean active) throws SQLException {
        return jdbc.update("UPDATE services SET is_active = ? WHERE id = ?", toDb(active), id) == 1;
    }

    private static Service map(ResultSet rs) throws SQLException {
        return new Service(
                rs.getLong("id"),
                rs.getLong("device_categorie_id"),
                rs.getString("service_name"),
                rs.getLong("service_kind_id"),
                rs.getString("description"),
                getMoney(rs, "base_cost"),
                rs.getInt("estimated_duration_minutes"),
                getBoolean(rs, "is_active"));
    }
}
