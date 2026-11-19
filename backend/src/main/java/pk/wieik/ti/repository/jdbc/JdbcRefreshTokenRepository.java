package pk.wieik.ti.repository.jdbc;

import pk.wieik.ti.model.RefreshToken;
import pk.wieik.ti.repository.RefreshTokenRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import static pk.wieik.ti.repository.jdbc.SqlTypes.*;

final class JdbcRefreshTokenRepository extends JdbcRepository<RefreshToken> implements RefreshTokenRepository {

    JdbcRefreshTokenRepository(Jdbc jdbc) {
        super(jdbc, "refresh_tokens", JdbcRefreshTokenRepository::map);
    }

    @Override
    public Optional<RefreshToken> findByTokenHash(String tokenHash) {
        return findOneWhere("token_hash = ?", tokenHash);
    }

    @Override
    public boolean revoke(long id, LocalDateTime revokedAt) {
        return jdbc.update("UPDATE refresh_tokens SET revoked_at = ? WHERE id = ? AND revoked_at IS NULL",
                toDb(revokedAt), id) == 1;
    }

    @Override
    public int revokeFamily(String familyId, LocalDateTime revokedAt) {
        return jdbc.update("UPDATE refresh_tokens SET revoked_at = ? WHERE family_id = ? AND revoked_at IS NULL",
                toDb(revokedAt), familyId);
    }

    @Override
    public int revokeAllForUser(long userId, LocalDateTime revokedAt) {
        return jdbc.update("UPDATE refresh_tokens SET revoked_at = ? WHERE user_id = ? AND revoked_at IS NULL",
                toDb(revokedAt), userId);
    }

    @Override
    public int deleteExpired(LocalDateTime now) {
        return jdbc.update("DELETE FROM refresh_tokens WHERE expires_at <= ?", toDb(now));
    }

    @Override
    public RefreshToken insert(RefreshToken token) {
        long id = jdbc.insert("""
                INSERT INTO refresh_tokens (user_id, token_hash, family_id, created_at, expires_at, revoked_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """,
                token.userId(), token.tokenHash(), token.familyId(),
                toDb(token.createdAt()), toDb(token.expiresAt()), toDb(token.revokedAt()));
        return token.withId(id);
    }

    @Override
    public boolean update(RefreshToken token) {
        return jdbc.update("UPDATE refresh_tokens SET revoked_at = ? WHERE id = ?",
                toDb(token.revokedAt()), token.id()) == 1;
    }

    private static RefreshToken map(ResultSet rs) throws SQLException {
        return new RefreshToken(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getString("token_hash"),
                rs.getString("family_id"),
                getDateTime(rs, "created_at"),
                getDateTime(rs, "expires_at"),
                getDateTime(rs, "revoked_at"));
    }
}
