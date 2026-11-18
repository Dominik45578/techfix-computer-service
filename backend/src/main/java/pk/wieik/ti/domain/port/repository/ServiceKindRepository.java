package pk.wieik.ti.domain.port.repository;

import pk.wieik.ti.domain.model.catalog.ServiceKind;

import java.util.List;

public interface ServiceKindRepository extends CrudRepository<ServiceKind> {

    List<ServiceKind> findAllByName();
}
