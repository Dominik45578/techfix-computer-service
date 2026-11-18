package pk.wieik.ti.persistence.repository;

import pk.wieik.ti.domain.model.order.PaymentStatus;
import pk.wieik.ti.domain.model.order.Purchase;
import pk.wieik.ti.domain.port.repository.PurchaseRepository;
import pk.wieik.ti.persistence.jdbc.Jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static pk.wieik.ti.persistence.jdbc.SqlTypes.*;

public final class JdbcPurchaseRepository extends JdbcRepository<Purchase> implements PurchaseRepository {

    public JdbcPurchaseRepository(Jdbc jdbc) {
        super(jdbc, "purchases", JdbcPurchaseRepository::map);
    }

    @Override
    public List<Purchase> findByUserId(long userId) {
        return findWhere("user_id = ? ORDER BY created_at DESC, id DESC", userId);
    }

    @Override
    public List<Purchase> findByPaymentStatus(PaymentStatus status) {
        return findWhere("payment_status = ? ORDER BY created_at, id", toDb(status));
    }

    @Override
    public Purchase insert(Purchase purchase) {
        long id = jdbc.insert("""
                INSERT INTO purchases (user_id, service_id, total_value, payment_status, created_at)
                VALUES (?, ?, ?, ?, ?)
                """,
                purchase.userId(), purchase.serviceId(), toDb(purchase.totalValue()),
                toDb(purchase.paymentStatus()), toDb(purchase.createdAt()));
        return purchase.withId(id);
    }

    /** Zmienia kwotę i status płatności; klient, usługa i data zakupu są niezmienne. */
    @Override
    public boolean update(Purchase purchase) {
        return jdbc.update("UPDATE purchases SET total_value = ?, payment_status = ? WHERE id = ?",
                toDb(purchase.totalValue()), toDb(purchase.paymentStatus()), purchase.id()) == 1;
    }

    @Override
    public boolean updatePaymentStatus(long id, PaymentStatus status) {
        return jdbc.update("UPDATE purchases SET payment_status = ? WHERE id = ?",
                toDb(status), id) == 1;
    }

    private static Purchase map(ResultSet rs) throws SQLException {
        return new Purchase(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getLong("service_id"),
                getMoney(rs, "total_value"),
                getEnum(rs, "payment_status", PaymentStatus.class),
                getDateTime(rs, "created_at"));
    }
}
