package pk.wieik.ti.repository;

import pk.wieik.ti.model.DeviceCategory;

import java.util.List;

public interface DeviceCategoryRepository extends CrudRepository<DeviceCategory> {

    List<DeviceCategory> findAllByName();
}
