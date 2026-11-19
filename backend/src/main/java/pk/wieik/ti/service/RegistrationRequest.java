package pk.wieik.ti.service;

import java.time.LocalDate;


public record RegistrationRequest(String email, String password, String firstName, String lastName,
                                  String phoneNumber, LocalDate dateOfBirth) {
    @Override
    public String toString() {
        return "RegistrationRequest[email=" + email + "]";
    }
}
