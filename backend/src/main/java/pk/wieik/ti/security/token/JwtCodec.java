package pk.wieik.ti.security.token;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.DefaultJOSEObjectTypeVerifier;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimNames;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class JwtCodec {
    public static final int MIN_SECRET_BYTES = 32;

    private static final String ISSUER = "ti-serwis";
    private static final JWSAlgorithm ALGORITHM = JWSAlgorithm.HS256;
    private static final Set<String> STANDARD_CLAIMS = Set.of(
            JWTClaimNames.SUBJECT, JWTClaimNames.ISSUED_AT, JWTClaimNames.EXPIRATION_TIME, JWTClaimNames.JWT_ID);

    private final byte[] secret;
    private final JWSSigner signer;

    public JwtCodec(byte[] secret) {
        if (secret == null || secret.length < MIN_SECRET_BYTES) {
            throw new IllegalArgumentException("Sekret JWT musi mieć co najmniej " + MIN_SECRET_BYTES + " bajty");
        }
        this.secret = secret.clone();
        try {
            this.signer = new MACSigner(this.secret);
        } catch (JOSEException ex) {
            throw new IllegalArgumentException("Nieprawidłowy sekret JWT", ex);
        }
    }

    IssuedToken issue(JOSEObjectType type, long userId, Duration lifetime, Map<String, String> claims) {
        Instant now = Instant.now();
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                .issuer(ISSUER)
                .subject(Long.toString(userId))
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plus(lifetime)))
                .jwtID(UUID.randomUUID().toString());
        claims.forEach(builder::claim);

        SignedJWT jwt = new SignedJWT(new JWSHeader.Builder(ALGORITHM).type(type).build(), builder.build());
        try {
            jwt.sign(signer);
        } catch (JOSEException ex) {
            throw new IllegalStateException("Nie można podpisać JWT", ex);
        }
        return new IssuedToken(jwt.serialize(), lifetime);
    }

    JwtVerifier verifier(JOSEObjectType type, Set<String> customClaims) {
        Set<String> requiredClaims = new HashSet<>(STANDARD_CLAIMS);
        requiredClaims.addAll(customClaims);

        DefaultJWTProcessor<SecurityContext> processor = new DefaultJWTProcessor<>();
        processor.setJWSTypeVerifier(new DefaultJOSEObjectTypeVerifier<>(type));
        processor.setJWSKeySelector(new JWSVerificationKeySelector<>(ALGORITHM, new ImmutableSecret<>(secret)));
        processor.setJWTClaimsSetVerifier(new DefaultJWTClaimsVerifier<>(
                new JWTClaimsSet.Builder().issuer(ISSUER).build(), requiredClaims));
        return new JwtVerifier(processor);
    }
}
