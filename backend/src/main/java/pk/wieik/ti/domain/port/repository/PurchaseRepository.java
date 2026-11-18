package pk.wieik.ti.domain.port.repository;

import pk.wieik.ti.domain.model.order.PaymentStatus;
import pk.wieik.ti.domain.model.order.Purchase;

import java.util.List;

public interface PurchaseRepository extends CrudRepository<Purchase> {

    /** Od najnowszych. */
    List<Purchase> findByUserId(long userId);

    /** Od najstarszych (kolejka do obsłużenia). */
    List<Purchase> findByPaymentStatus(PaymentStatus status);

    boolean updatePaymentStatus(long id, PaymentStatus status);
}
