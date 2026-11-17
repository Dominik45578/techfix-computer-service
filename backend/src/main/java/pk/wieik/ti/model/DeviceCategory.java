package pk.wieik.ti.model;

public record DeviceCategory(long id, String deviceName, String description) {
    public DeviceCategory {
        Checks.notBlank(deviceName, "deviceName");
    }

    public static DeviceCategory of(String deviceName, String description) {
        return new DeviceCategory(0, deviceName, description);
    }
}
