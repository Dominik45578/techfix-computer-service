package pk.wieik.ti.controller.dto;

import pk.wieik.ti.service.RegistrationRequest;

import java.time.LocalDate;

public record RegisterRequest(String email, String password, String firstName, String lastName,
                              String phoneNumber, LocalDate dateOfBirth) {
    public RegisterRequest {
        RequestValidator.notBlank(email, "email");
        RequestValidator.notBlank(password, "password");
        RequestValidator.notBlank(firstName, "firstName");
        RequestValidator.notBlank(lastName, "lastName");
    }

    public RegistrationRequest toRegistrationRequest() {
        return new RegistrationRequest(email, password, firstName, lastName, phoneNumber, dateOfBirth);
    }

    @Override
    public String toString() {
        return "RegisterRequest[email=" + email + "]";
    }
}
