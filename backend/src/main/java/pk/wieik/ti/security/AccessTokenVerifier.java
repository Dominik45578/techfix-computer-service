package pk.wieik.ti.security;

import pk.wieik.ti.service.AuthenticatedUser;

import java.util.Optional;

@FunctionalInterface
public interface AccessTokenVerifier {
    Optional<AuthenticatedUser> verify(String token);
}
