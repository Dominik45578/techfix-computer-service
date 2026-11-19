package pk.wieik.ti.controller.dto;

import java.time.LocalDate;

public record UpdateProfileRequest(String firstName, String lastName, String phoneNumber, LocalDate dateOfBirth) {
    public UpdateProfileRequest {
        RequestValidator.notBlank(firstName, "firstName");
        RequestValidator.notBlank(lastName, "lastName");
    }
}
