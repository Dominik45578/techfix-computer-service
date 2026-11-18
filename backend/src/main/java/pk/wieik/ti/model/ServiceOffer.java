package pk.wieik.ti.model;


import java.math.BigDecimal;

/**
 * Tabela {@code services}: pozycja z katalogu usług serwisu (np. „Wymiana matrycy”).
 * Nazwa {@code ServiceOffer} zamiast {@code Service}, żeby nie myliła się z warstwą serwisów.
 */
public record ServiceOffer(long id, long deviceCategoryId, String serviceName, long serviceKindId,
                           String description, BigDecimal baseCost,
                           int estimatedDurationMinutes, boolean active) {

    public ServiceOffer {
        Checks.positiveId(deviceCategoryId, "deviceCategoryId");
        Checks.notBlank(serviceName, "serviceName");
        Checks.positiveId(serviceKindId, "serviceKindId");
        Checks.notBlank(description, "description");
        Checks.nonNegative(baseCost, "baseCost");
        Checks.positive(estimatedDurationMinutes, "estimatedDurationMinutes");
    }

    /** Nowa, aktywna usługa (id = 0). */
    public static ServiceOffer of(long deviceCategoryId, String serviceName, long serviceKindId,
                                  String description, BigDecimal baseCost, int estimatedDurationMinutes) {
        return new ServiceOffer(0, deviceCategoryId, serviceName, serviceKindId,
                description, baseCost, estimatedDurationMinutes, true);
    }

    public ServiceOffer withId(long newId) {
        return new ServiceOffer(newId, deviceCategoryId, serviceName, serviceKindId,
                description, baseCost, estimatedDurationMinutes, active);
    }

    public ServiceOffer withActive(boolean newActive) {
        return new ServiceOffer(id, deviceCategoryId, serviceName, serviceKindId,
                description, baseCost, estimatedDurationMinutes, newActive);
    }
}
