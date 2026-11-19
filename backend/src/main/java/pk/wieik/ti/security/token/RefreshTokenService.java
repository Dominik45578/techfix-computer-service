package pk.wieik.ti.security.token;

import com.nimbusds.jose.JOSEObjectType;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class RefreshTokenService {
    private static final JOSEObjectType TYPE = new JOSEObjectType("refresh+jwt");
    private static final Duration LIFETIME = Duration.ofDays(7);

    private final JwtCodec jwt;
    private final JwtVerifier verifier;

    public RefreshTokenService(JwtCodec jwt) {
        this.jwt = jwt;
        this.verifier = jwt.verifier(TYPE, Set.of());
    }

    public IssuedToken issue(long userId) {
        return jwt.issue(TYPE, userId, LIFETIME, Map.of());
    }

    public Optional<Long> verify(String token) {
        return verifier.verify(token).flatMap(JwtVerifier::userId);
    }
}
