package pk.wieik.ti.security;

import pk.wieik.ti.service.AuthenticatedUser;

import java.util.Optional;

/** Weryfikacja access tokenu: ważny token → użytkownik z claimów, każdy inny → {@link Optional#empty()}. */
@FunctionalInterface
public interface AccessTokenVerifier {
    Optional<AuthenticatedUser> verify(String token);
}
