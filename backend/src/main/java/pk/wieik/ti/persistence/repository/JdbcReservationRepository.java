package pk.wieik.ti.persistence.repository;

import pk.wieik.ti.domain.model.order.Reservation;
import pk.wieik.ti.domain.model.order.ReservationStatus;
import pk.wieik.ti.domain.port.repository.ReservationRepository;
import pk.wieik.ti.persistence.jdbc.Jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static pk.wieik.ti.persistence.jdbc.SqlTypes.*;

public final class JdbcReservationRepository extends JdbcRepository<Reservation>
        implements ReservationRepository {

    public JdbcReservationRepository(Jdbc jdbc) {
        super(jdbc, "reservations", JdbcReservationRepository::map);
    }

    @Override
    public Optional<Reservation> findByPurchaseId(long purchaseId) {
        return findOneWhere("purchase_id = ?", purchaseId);
    }

    @Override
    public List<Reservation> findByUserId(long userId) {
        return jdbc.query("""
                SELECT r.* FROM reservations r
                JOIN purchases p ON p.id = r.purchase_id
                WHERE p.user_id = ?
                ORDER BY r.planned_at DESC
                """, mapper, userId);
    }

    @Override
    public List<Reservation> findByAssignedStaffId(long staffId) {
        return findWhere("assigned_staff_id = ? ORDER BY planned_at", staffId);
    }

    @Override
    public List<Reservation> findByStatus(ReservationStatus status) {
        return findWhere("status = ? ORDER BY planned_at", toDb(status));
    }

    @Override
    public List<Reservation> findPlannedBetween(LocalDateTime from, LocalDateTime to) {
        return findWhere("planned_at >= ? AND planned_at < ? ORDER BY planned_at",
                toDb(from), toDb(to));
    }

    @Override
    public Reservation insert(Reservation r) {
        long id = jdbc.insert("""
                INSERT INTO reservations (purchase_id, assigned_staff_id, status, planned_at,
                                          notes, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """,
                r.purchaseId(), r.assignedStaffId(), toDb(r.status()), toDb(r.plannedAt()),
                r.notes(), toDb(r.createdAt()), toDb(r.updatedAt()));
        return r.withId(id);
    }

    @Override
    public boolean update(Reservation r) {
        return jdbc.update("""
                UPDATE reservations
                SET assigned_staff_id = ?, status = ?, planned_at = ?, notes = ?, updated_at = ?
                WHERE id = ?
                """,
                r.assignedStaffId(), toDb(r.status()), toDb(r.plannedAt()), r.notes(),
                toDb(r.updatedAt()), r.id()) == 1;
    }

    private static Reservation map(ResultSet rs) throws SQLException {
        return new Reservation(
                rs.getLong("id"),
                rs.getLong("purchase_id"),
                getNullableLong(rs, "assigned_staff_id"),
                getEnum(rs, "status", ReservationStatus.class),
                getDateTime(rs, "planned_at"),
                rs.getString("notes"),
                getDateTime(rs, "created_at"),
                getDateTime(rs, "updated_at"));
    }
}
