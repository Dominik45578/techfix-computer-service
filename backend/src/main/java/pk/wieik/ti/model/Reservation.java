package pk.wieik.ti.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public record Reservation(long id, long purchaseId, Long assignedStaffId, ReservationStatus status, 
                          LocalDateTime plannedAt, String notes, LocalDateTime createdAt, LocalDateTime updatedAt) {
                            
    public Reservation {
        Checks.positiveId(purchaseId, "purchaseId");
        if (assignedStaffId != null) {
            Checks.positiveId(assignedStaffId, "assignedStaffId");
        }
        Checks.notNull(status, "status");
        Checks.notNull(plannedAt, "plannedAt");
        Checks.notNull(createdAt, "createdAt");
        Checks.notNull(updatedAt, "updatedAt");
    }

    public static Reservation of(long purchaseId, LocalDateTime plannedAt, String notes) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        return new Reservation(0, purchaseId, null, ReservationStatus.PENDING,
                plannedAt, notes, now, now);
    }

    public Reservation withStatus(ReservationStatus newStatus) {
        return new Reservation(id, purchaseId, assignedStaffId, newStatus,
                plannedAt, notes, createdAt, now());
    }

    public Reservation withAssignedStaff(Long staffId) {
        return new Reservation(id, purchaseId, staffId, status,
                plannedAt, notes, createdAt, now());
    }

    public Reservation withPlannedAt(LocalDateTime newPlannedAt) {
        return new Reservation(id, purchaseId, assignedStaffId, status,
                newPlannedAt, notes, createdAt, now());
    }

    private static LocalDateTime now() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }
}
