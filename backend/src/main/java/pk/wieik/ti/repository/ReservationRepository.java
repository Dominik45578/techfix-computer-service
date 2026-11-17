package pk.wieik.ti.repository;

import pk.wieik.ti.db.Jdbc;
import pk.wieik.ti.model.Reservation;
import pk.wieik.ti.model.ReservationStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static pk.wieik.ti.db.SqlTypes.*;

public class ReservationRepository extends JdbcRepository<Reservation> {
    public ReservationRepository(Jdbc jdbc) {
        super(jdbc, "reservations", ReservationRepository::map);
    }

    public Optional<Reservation> findByPurchaseId(long purchaseId) throws SQLException {
        return findOneWhere("purchase_id = ?", purchaseId);
    }

    public List<Reservation> findByUserId(long userId) throws SQLException {
        return jdbc.query("""
                SELECT r.* FROM reservations r
                JOIN purchases p ON p.id = r.purchase_id
                WHERE p.user_id = ?
                ORDER BY r.planned_at DESC
                """, ReservationRepository::map, userId);
    }

    public List<Reservation> findByAssignedStaffId(long staffId) throws SQLException {
        return jdbc.query("SELECT * FROM reservations WHERE assigned_staff_id = ? ORDER BY planned_at",
                ReservationRepository::map, staffId);
    }

    public List<Reservation> findByStatus(ReservationStatus status) throws SQLException {
        return jdbc.query("SELECT * FROM reservations WHERE status = ? ORDER BY planned_at",
                ReservationRepository::map, toDb(status));
    }

    public List<Reservation> findPlannedBetween(LocalDateTime from, LocalDateTime to) throws SQLException {
        return jdbc.query("""
                SELECT * FROM reservations
                WHERE planned_at >= ? AND planned_at < ?
                ORDER BY planned_at
                """, ReservationRepository::map, toDb(from), toDb(to));
    }

    public Reservation insert(Reservation r) throws SQLException {
        long id = jdbc.insert("""
                INSERT INTO reservations (purchase_id, assigned_staff_id, status, planned_at,
                                          notes, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """,
                r.purchaseId(), r.assignedStaffId(), toDb(r.status()), toDb(r.plannedAt()),
                r.notes(), toDb(r.createdAt()), toDb(r.updatedAt()));
        return new Reservation(id, r.purchaseId(), r.assignedStaffId(), r.status(),
                r.plannedAt(), r.notes(), r.createdAt(), r.updatedAt());
    }

    public boolean update(Reservation r) throws SQLException {
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
