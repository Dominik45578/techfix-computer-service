package pk.wieik.ti.controller;

import jakarta.servlet.http.HttpServletResponse;

/** Błąd z konkretnym statusem HTTP. {@link JsonServlet} zamienia go na {@link ApiError}. */
class ApiException extends RuntimeException {
    private final int status;

    ApiException(int status, String message) {
        super(message);
        this.status = status;
    }

    static ApiException badRequest(String message) {
        return new ApiException(HttpServletResponse.SC_BAD_REQUEST, message);
    }

    int status() {
        return status;
    }
}
