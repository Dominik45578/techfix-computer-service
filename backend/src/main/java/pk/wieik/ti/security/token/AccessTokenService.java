package pk.wieik.ti.security.token;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jwt.JWTClaimsSet;
import pk.wieik.ti.model.UserRole;
import pk.wieik.ti.security.AccessTokenVerifier;
import pk.wieik.ti.service.AuthenticatedUser;

import java.text.ParseException;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class AccessTokenService implements AccessTokenVerifier {
    private static final JOSEObjectType TYPE = new JOSEObjectType("at+jwt");
    private static final Duration LIFETIME = Duration.ofMinutes(15);
    private static final String EMAIL = "email";
    private static final String NAME = "name";
    private static final String ROLE = "role";

    private final JwtCodec jwt;
    private final JwtVerifier verifier;

    public AccessTokenService(JwtCodec jwt) {
        this.jwt = jwt;
        this.verifier = jwt.verifier(TYPE, Set.of(EMAIL, NAME, ROLE));
    }

    public IssuedToken issue(AuthenticatedUser user) {
        return jwt.issue(TYPE, user.id(), LIFETIME, Map.of(
                EMAIL, user.email(),
                NAME, user.fullName(),
                ROLE, user.role().name()));
    }

    @Override
    public Optional<AuthenticatedUser> verify(String token) {
        return verifier.verify(token).flatMap(AccessTokenService::toUser);
    }

    private static Optional<AuthenticatedUser> toUser(JWTClaimsSet claims) {
        return JwtVerifier.userId(claims).flatMap(id -> {
            try {
                return Optional.of(new AuthenticatedUser(id,
                        claims.getStringClaim(EMAIL),
                        claims.getStringClaim(NAME),
                        UserRole.valueOf(claims.getStringClaim(ROLE))));
            } catch (ParseException | IllegalArgumentException ex) {
                return Optional.empty();
            }
        });
    }
}
