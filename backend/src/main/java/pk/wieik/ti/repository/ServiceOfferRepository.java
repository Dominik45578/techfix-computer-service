package pk.wieik.ti.repository;

import pk.wieik.ti.model.ServiceOffer;

import java.util.List;

public interface ServiceOfferRepository extends CrudRepository<ServiceOffer> {

    List<ServiceOffer> findActive();

    List<ServiceOffer> findActiveByDeviceCategory(long deviceCategoryId);

    List<ServiceOffer> findActiveByServiceKind(long serviceKindId);

    boolean setActive(long id, boolean active);
}
