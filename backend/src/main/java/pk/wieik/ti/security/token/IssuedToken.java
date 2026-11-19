package pk.wieik.ti.security.token;

import java.time.Duration;

public record IssuedToken(String value, Duration lifetime) {}
