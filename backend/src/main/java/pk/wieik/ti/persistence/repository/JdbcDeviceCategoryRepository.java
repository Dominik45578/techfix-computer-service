package pk.wieik.ti.persistence.repository;

import pk.wieik.ti.domain.model.catalog.DeviceCategory;
import pk.wieik.ti.domain.port.repository.DeviceCategoryRepository;
import pk.wieik.ti.persistence.jdbc.Jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class JdbcDeviceCategoryRepository extends JdbcRepository<DeviceCategory>
        implements DeviceCategoryRepository {

    public JdbcDeviceCategoryRepository(Jdbc jdbc) {
        super(jdbc, "device_categories", JdbcDeviceCategoryRepository::map);
    }

    @Override
    public List<DeviceCategory> findAllByName() {
        return jdbc.query("SELECT * FROM device_categories ORDER BY device_name", mapper);
    }

    @Override
    public DeviceCategory insert(DeviceCategory category) {
        long id = jdbc.insert(
                "INSERT INTO device_categories (device_name, description) VALUES (?, ?)",
                category.deviceName(), category.description());
        return category.withId(id);
    }

    @Override
    public boolean update(DeviceCategory category) {
        return jdbc.update(
                "UPDATE device_categories SET device_name = ?, description = ? WHERE id = ?",
                category.deviceName(), category.description(), category.id()) == 1;
    }

    private static DeviceCategory map(ResultSet rs) throws SQLException {
        return new DeviceCategory(
                rs.getLong("id"),
                rs.getString("device_name"),
                rs.getString("description"));
    }
}
