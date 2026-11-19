package pk.wieik.ti.controller.dto;

public record ChangePasswordRequest(String currentPassword, String newPassword, String confirmNewPassword) {
    public ChangePasswordRequest {
        RequestValidator.notBlank(currentPassword, "currentPassword");
        RequestValidator.notBlank(newPassword, "newPassword");
        RequestValidator.notBlank(confirmNewPassword, "confirmNewPassword");
        RequestValidator.equals(newPassword, confirmNewPassword, "newPassword", "confirmNewPassword");
    }

    @Override
    public String toString() {
        return "ChangePasswordRequest[]";
    }
}
