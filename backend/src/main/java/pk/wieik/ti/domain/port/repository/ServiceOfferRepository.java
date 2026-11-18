package pk.wieik.ti.domain.port.repository;

import pk.wieik.ti.domain.model.catalog.ServiceOffer;

import java.util.List;

public interface ServiceOfferRepository extends CrudRepository<ServiceOffer> {

    List<ServiceOffer> findActive();

    List<ServiceOffer> findActiveByDeviceCategory(long deviceCategoryId);

    List<ServiceOffer> findActiveByServiceKind(long serviceKindId);

    boolean setActive(long id, boolean active);
}
