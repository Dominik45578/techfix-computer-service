package pk.wieik.ti.security.token;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.JWTProcessor;

import java.text.ParseException;
import java.util.Optional;

final class JwtVerifier {
    private final JWTProcessor<SecurityContext> processor;

    JwtVerifier(JWTProcessor<SecurityContext> processor) {
        this.processor = processor;
    }

    Optional<JWTClaimsSet> verify(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(processor.process(token, null));
        } catch (ParseException | BadJOSEException | JOSEException ex) {
            return Optional.empty();
        }
    }

    static Optional<Long> userId(JWTClaimsSet claims) {
        try {
            return Optional.of(Long.parseLong(claims.getSubject()));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }
}
