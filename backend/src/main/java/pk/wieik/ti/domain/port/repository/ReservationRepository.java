package pk.wieik.ti.domain.port.repository;

import pk.wieik.ti.domain.model.order.Reservation;
import pk.wieik.ti.domain.model.order.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends CrudRepository<Reservation> {

    Optional<Reservation> findByPurchaseId(long purchaseId);

    /** Rezerwacje klienta (przez jego zakupy), od najpóźniejszego terminu. */
    List<Reservation> findByUserId(long userId);

    List<Reservation> findByAssignedStaffId(long staffId);

    List<Reservation> findByStatus(ReservationStatus status);

    /** Przedział {@code [from, to)}. */
    List<Reservation> findPlannedBetween(LocalDateTime from, LocalDateTime to);
}
