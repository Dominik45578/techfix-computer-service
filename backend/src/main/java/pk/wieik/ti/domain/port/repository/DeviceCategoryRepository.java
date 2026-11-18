package pk.wieik.ti.domain.port.repository;

import pk.wieik.ti.domain.model.catalog.DeviceCategory;

import java.util.List;

public interface DeviceCategoryRepository extends CrudRepository<DeviceCategory> {

    List<DeviceCategory> findAllByName();
}
