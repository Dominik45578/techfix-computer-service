package pk.wieik.ti.persistence.repository;

import pk.wieik.ti.domain.model.catalog.ServiceOffer;
import pk.wieik.ti.domain.port.repository.ServiceOfferRepository;
import pk.wieik.ti.persistence.jdbc.Jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static pk.wieik.ti.persistence.jdbc.SqlTypes.*;

/** Kolumna {@code device_categorie_id} ma literówkę w migracji V1, zostawiamy ją tak jak w bazie. */
public final class JdbcServiceOfferRepository extends JdbcRepository<ServiceOffer>
        implements ServiceOfferRepository {

    public JdbcServiceOfferRepository(Jdbc jdbc) {
        super(jdbc, "services", JdbcServiceOfferRepository::map);
    }

    @Override
    public List<ServiceOffer> findActive() {
        return findWhere("is_active = 1 ORDER BY service_name");
    }

    @Override
    public List<ServiceOffer> findActiveByDeviceCategory(long deviceCategoryId) {
        return findWhere("is_active = 1 AND device_categorie_id = ? ORDER BY service_name",
                deviceCategoryId);
    }

    @Override
    public List<ServiceOffer> findActiveByServiceKind(long serviceKindId) {
        return findWhere("is_active = 1 AND service_kind_id = ? ORDER BY service_name",
                serviceKindId);
    }

    @Override
    public ServiceOffer insert(ServiceOffer offer) {
        long id = jdbc.insert("""
                INSERT INTO services (device_categorie_id, service_name, service_kind_id, description,
                                      base_cost, estimated_duration_minutes, is_active)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """,
                offer.deviceCategoryId(), offer.serviceName(), offer.serviceKindId(),
                offer.description(), toDb(offer.baseCost()),
                offer.estimatedDurationMinutes(), toDb(offer.active()));
        return offer.withId(id);
    }

    @Override
    public boolean update(ServiceOffer offer) {
        return jdbc.update("""
                UPDATE services
                SET device_categorie_id = ?, service_name = ?, service_kind_id = ?, description = ?,
                    base_cost = ?, estimated_duration_minutes = ?, is_active = ?
                WHERE id = ?
                """,
                offer.deviceCategoryId(), offer.serviceName(), offer.serviceKindId(),
                offer.description(), toDb(offer.baseCost()),
                offer.estimatedDurationMinutes(), toDb(offer.active()),
                offer.id()) == 1;
    }

    @Override
    public boolean setActive(long id, boolean active) {
        return jdbc.update("UPDATE services SET is_active = ? WHERE id = ?", toDb(active), id) == 1;
    }

    private static ServiceOffer map(ResultSet rs) throws SQLException {
        return new ServiceOffer(
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
