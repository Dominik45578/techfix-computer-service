package pk.wieik.ti.model;

import java.math.BigDecimal;

public record Service(long id,  long deviceCategoryId, String serviceName, long serviceKindId,
                      String description, BigDecimal baseCost,
                      int estimatedDurationMinutes,
                      boolean active) {

    public Service {
        Checks.positiveId(deviceCategoryId, "deviceCategoryId");
        Checks.notBlank(serviceName, "serviceName");
        Checks.positiveId(serviceKindId, "serviceKindId");
        Checks.notBlank(description, "description");
        Checks.nonNegative(baseCost, "baseCost");
        Checks.positive(estimatedDurationMinutes, "estimatedDurationMinutes");
    }

    public static Service of(long deviceCategoryId, String serviceName, long serviceKindId,
                             String description, BigDecimal baseCost, int estimatedDurationMinutes) {
        return new Service(0, deviceCategoryId, serviceName, serviceKindId,
                description, baseCost, estimatedDurationMinutes, true);
    }
}
