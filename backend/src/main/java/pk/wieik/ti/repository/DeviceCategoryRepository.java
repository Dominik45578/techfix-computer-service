package pk.wieik.ti.repository;

import pk.wieik.ti.db.Jdbc;
import pk.wieik.ti.model.DeviceCategory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DeviceCategoryRepository extends JdbcRepository<DeviceCategory> {
    public DeviceCategoryRepository(Jdbc jdbc) {
        super(jdbc, "device_categories", DeviceCategoryRepository::map);
    }

    public List<DeviceCategory> findAllByName() throws SQLException {
        return jdbc.query("SELECT * FROM device_categories ORDER BY device_name",
                DeviceCategoryRepository::map);
    }

    public DeviceCategory insert(DeviceCategory category) throws SQLException {
        long id = jdbc.insert(
                "INSERT INTO device_categories (device_name, description) VALUES (?, ?)",
                category.deviceName(), category.description());
        return new DeviceCategory(id, category.deviceName(), category.description());
    }

    public boolean update(DeviceCategory category) throws SQLException {
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
