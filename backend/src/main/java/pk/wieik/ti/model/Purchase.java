package pk.wieik.ti.model;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/** Tabela {@code purchases}. */
public record Purchase(long id, long userId, long serviceId, BigDecimal totalValue,
                       PaymentStatus paymentStatus, LocalDateTime createdAt) {

    public Purchase {
        Checks.positiveId(userId, "userId");
        Checks.positiveId(serviceId, "serviceId");
        Checks.nonNegative(totalValue, "totalValue");
        Checks.notNull(paymentStatus, "paymentStatus");
        Checks.notNull(createdAt, "createdAt");
    }

    /** Nowy zakup oczekujący na płatność (id = 0). */
    public static Purchase of(long userId, long serviceId, BigDecimal totalValue) {
        return new Purchase(0, userId, serviceId, totalValue,
                PaymentStatus.PENDING, LocalDateTime.now(ZoneOffset.UTC));
    }

    public Purchase withId(long newId) {
        return new Purchase(newId, userId, serviceId, totalValue, paymentStatus, createdAt);
    }

    public Purchase withPaymentStatus(PaymentStatus newStatus) {
        return new Purchase(id, userId, serviceId, totalValue, newStatus, createdAt);
    }
}
