package pk.wieik.ti.controller.dto;

import pk.wieik.ti.security.token.IssuedToken;

public record TokenResponse(String accessToken, String tokenType, long expiresIn) {
    private static final String BEARER = "Bearer";

    public static TokenResponse bearer(IssuedToken accessToken) {
        return new TokenResponse(accessToken.value(), BEARER, accessToken.lifetime().toSeconds());
    }
}
