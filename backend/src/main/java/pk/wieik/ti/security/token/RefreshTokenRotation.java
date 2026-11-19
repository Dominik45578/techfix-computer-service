package pk.wieik.ti.security.token;

public record RefreshTokenRotation(long userId, IssuedToken refreshToken) {}
