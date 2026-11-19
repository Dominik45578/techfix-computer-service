package pk.wieik.ti.controller.dto;

import java.time.LocalDate;

public record RegisterRequest(String email, String password, String firstName, String lastName,
                              String phoneNumber, LocalDate dateOfBirth) {
    public RegisterRequest {
        RequestValidator.notBlank(email, "email");
        RequestValidator.notBlank(password, "password");
        RequestValidator.notBlank(firstName, "firstName");
        RequestValidator.notBlank(lastName, "lastName");
    }
}
