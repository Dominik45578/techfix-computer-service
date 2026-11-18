package pk.wieik.ti.domain.model.catalog;

import pk.wieik.ti.domain.model.Checks;

/** Tabela {@code service_kind}. */
public record ServiceKind(long id, String kindName, String description) {

    public ServiceKind {
        Checks.notBlank(kindName, "kindName");
        Checks.notBlank(description, "description");
    }

    public static ServiceKind of(String kindName, String description) {
        return new ServiceKind(0, kindName, description);
    }

    public ServiceKind withId(long newId) {
        return new ServiceKind(newId, kindName, description);
    }
}
