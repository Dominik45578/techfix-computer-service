package pk.wieik.ti.model;

public record ServiceKind(long id, String kindName, String description) {
    public ServiceKind {
        Checks.notBlank(kindName, "kindName");
        Checks.notBlank(description, "description");
    }

    public static ServiceKind of(String kindName, String description) {
        return new ServiceKind(0, kindName, description);
    }
}
