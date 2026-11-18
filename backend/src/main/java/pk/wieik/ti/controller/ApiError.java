package pk.wieik.ti.controller;

import java.time.Instant;

/**
 * Treść każdej odpowiedzi z błędem. Frontend zawsze dostaje ten sam kształt:
 * <pre>{"timestamp": "2026-11-18T21:46:53Z", "status": 404, "message": "...", "path": "/api/..."}</pre>
 */
public record ApiError(Instant timestamp, int status, String message, String path) {

    static ApiError of(int status, String message, String path) {
        return new ApiError(Instant.now(), status, message, path);
    }
}
