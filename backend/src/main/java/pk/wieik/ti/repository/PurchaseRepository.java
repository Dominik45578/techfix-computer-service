package pk.wieik.ti.repository;

import pk.wieik.ti.db.Jdbc;
import pk.wieik.ti.model.PaymentStatus;
import pk.wieik.ti.model.Purchase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static pk.wieik.ti.db.SqlTypes.*;

public class PurchaseRepository extends JdbcRepository<Purchase> {
    public PurchaseRepository(Jdbc jdbc) {
        super(jdbc, "purchases", PurchaseRepository::map);
    }

    public List<Purchase> findByUserId(long userId) throws SQLException {
        return jdbc.query("SELECT * FROM purchases WHERE user_id = ? ORDER BY created_at DESC, id DESC",
                PurchaseRepository::map, userId);
    }

    public List<Purchase> findByPaymentStatus(PaymentStatus status) throws SQLException {
        return jdbc.query("SELECT * FROM purchases WHERE payment_status = ? ORDER BY created_at, id",
                PurchaseRepository::map, toDb(status));
    }

    public Purchase insert(Purchase purchase) throws SQLException {
        long id = jdbc.insert("""
                INSERT INTO purchases (user_id, service_id, total_value, payment_status, created_at)
                VALUES (?, ?, ?, ?, ?)
                """,
                purchase.userId(), purchase.serviceId(), toDb(purchase.totalValue()),
                toDb(purchase.paymentStatus()), toDb(purchase.createdAt()));
        return new Purchase(id, purchase.userId(), purchase.serviceId(), purchase.totalValue(),
                purchase.paymentStatus(), purchase.createdAt());
    }

    public boolean updatePaymentStatus(long id, PaymentStatus status) throws SQLException {
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
