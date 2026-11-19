package pk.wieik.ti.repository;

import pk.wieik.ti.model.RefreshToken;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    /** @return {@code true} tylko gdy token był jeszcze aktywny; przy dwóch równoległych wywołaniach wygrywa jedno */
    boolean revoke(long id, LocalDateTime revokedAt);

    int revokeFamily(String familyId, LocalDateTime revokedAt);

    int deleteExpired(LocalDateTime now);
}
