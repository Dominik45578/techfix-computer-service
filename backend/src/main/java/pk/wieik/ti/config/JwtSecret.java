package pk.wieik.ti.config;

import jakarta.servlet.ServletContext;
import pk.wieik.ti.security.token.JwtCodec;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

final class JwtSecret {
    private static final String ENV_VARIABLE = "JWT_SECRET";

    private JwtSecret() {}

    static byte[] fromEnvironment(ServletContext servletContext) {
        String value = System.getenv(ENV_VARIABLE);
        if (value == null || value.isBlank()) {
            servletContext.log("Brak " + ENV_VARIABLE + ": używam losowego sekretu, tokeny wygasną po restarcie");
            return randomSecret();
        }
        byte[] secret = value.getBytes(StandardCharsets.UTF_8);
        if (secret.length < JwtCodec.MIN_SECRET_BYTES) {
            throw new IllegalStateException(
                    ENV_VARIABLE + " musi mieć co najmniej " + JwtCodec.MIN_SECRET_BYTES + " znaki");
        }
        return secret;
    }

    private static byte[] randomSecret() {
        byte[] secret = new byte[JwtCodec.MIN_SECRET_BYTES * 2];
        new SecureRandom().nextBytes(secret);
        return secret;
    }
}
