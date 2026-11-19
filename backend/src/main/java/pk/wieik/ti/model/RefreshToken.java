package pk.wieik.ti.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/** Tabela {@code refresh_tokens}. Sam token zna tylko klient, w bazie jest jego hash. */
public record RefreshToken(long id, long userId, String tokenHash, String familyId,
                           LocalDateTime createdAt, LocalDateTime expiresAt, LocalDateTime revokedAt) {

    public RefreshToken {
        Checks.positiveId(userId, "userId");
        Checks.notBlank(tokenHash, "tokenHash");
        Checks.notBlank(familyId, "familyId");
        Checks.notNull(createdAt, "createdAt");
        Checks.notNull(expiresAt, "expiresAt");
    }

    public static RefreshToken of(long userId, String tokenHash, String familyId, Duration lifetime) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        return new RefreshToken(0, userId, tokenHash, familyId, now, now.plus(lifetime), null);
    }

    public RefreshToken withId(long newId) {
        return new RefreshToken(newId, userId, tokenHash, familyId, createdAt, expiresAt, revokedAt);
    }

    public boolean isRevoked() {
        return revokedAt != null;
    }

    public boolean isExpiredAt(LocalDateTime now) {
        return !now.isBefore(expiresAt);
    }

    @Override
    public String toString() {
        return "RefreshToken[id=" + id + ", userId=" + userId + ", familyId=" + familyId + "]";
    }
}
