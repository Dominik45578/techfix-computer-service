package pk.wieik.ti.controller.dto;

public record LoginRequest(String email, String password) {
    public LoginRequest {
        RequestValidator.notBlank(email, "email");
        RequestValidator.notBlank(password, "password");
    }
}
