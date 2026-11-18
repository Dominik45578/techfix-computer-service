package pk.wieik.ti.repository;

import pk.wieik.ti.model.PaymentStatus;
import pk.wieik.ti.model.Purchase;

import java.util.List;

public interface PurchaseRepository extends CrudRepository<Purchase> {

    /** Od najnowszych. */
    List<Purchase> findByUserId(long userId);

    /** Od najstarszych (kolejka do obsłużenia). */
    List<Purchase> findByPaymentStatus(PaymentStatus status);

    boolean updatePaymentStatus(long id, PaymentStatus status);
}
