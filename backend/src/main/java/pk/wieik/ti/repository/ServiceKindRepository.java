package pk.wieik.ti.repository;

import pk.wieik.ti.model.ServiceKind;

import java.util.List;

public interface ServiceKindRepository extends CrudRepository<ServiceKind> {

    List<ServiceKind> findAllByName();
}
