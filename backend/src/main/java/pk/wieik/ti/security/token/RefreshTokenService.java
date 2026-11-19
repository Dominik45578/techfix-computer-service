package pk.wieik.ti.security.token;

import pk.wieik.ti.model.RefreshToken;
import pk.wieik.ti.repository.RefreshTokenRepository;
import pk.wieik.ti.repository.TransactionRunner;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;

/**
 * Nieprzezroczyste refresh tokeny z rotacją: każde odświeżenie unieważnia stary token i wydaje nowy
 * w tej samej rodzinie. Ponowne użycie unieważnionego tokena oznacza kradzież, więc cała rodzina wygasa.
 */
public final class RefreshTokenService {
    private static final Duration LIFETIME = Duration.ofDays(7);
    private static final int TOKEN_BYTES = 32;
    private static final int MAX_TOKEN_LENGTH = 64;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final RefreshTokenRepository tokens;
    private final TransactionRunner transactions;

    public RefreshTokenService(RefreshTokenRepository tokens, TransactionRunner transactions) {
        this.tokens = tokens;
        this.transactions = transactions;
    }

    public IssuedToken issue(long userId) {
        return transactions.inTransaction(() -> {
            tokens.deleteExpired(now());
            return store(userId, UUID.randomUUID().toString());
        });
    }

    public Optional<RefreshTokenRotation> rotate(String rawToken) {
        if (!isWellFormed(rawToken)) {
            return Optional.empty();
        }
        return transactions.inTransaction(() -> {
            Optional<RefreshToken> found = tokens.findByTokenHash(hash(rawToken));
            if (found.isEmpty()) {
                return Optional.empty();
            }
            RefreshToken token = found.get();
            LocalDateTime now = now();
            if (token.isExpiredAt(now)) {
                return Optional.empty();
            }
            if (token.isRevoked() || !tokens.revoke(token.id(), now)) {
                tokens.revokeFamily(token.familyId(), now);
                return Optional.empty();
            }
            return Optional.of(new RefreshTokenRotation(token.userId(), store(token.userId(), token.familyId())));
        });
    }

    public void revoke(String rawToken) {
        if (!isWellFormed(rawToken)) {
            return;
        }
        tokens.findByTokenHash(hash(rawToken))
                .ifPresent(token -> tokens.revokeFamily(token.familyId(), now()));
    }

    public void revokeAll(long userId) {
        tokens.revokeAllForUser(userId, now());
    }

    private IssuedToken store(long userId, String familyId) {
        String rawToken = generate();
        tokens.insert(RefreshToken.of(userId, hash(rawToken), familyId, LIFETIME));
        return new IssuedToken(rawToken, LIFETIME);
    }

    private static String generate() {
        byte[] bytes = new byte[TOKEN_BYTES];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String hash(String rawToken) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(rawToken.getBytes(StandardCharsets.US_ASCII));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 niedostępny", ex);
        }
    }

    private static boolean isWellFormed(String rawToken) {
        return rawToken != null && !rawToken.isBlank() && rawToken.length() <= MAX_TOKEN_LENGTH;
    }

    private static LocalDateTime now() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }
}
