package pk.wieik.ti.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public final class ErrorPage {
    private static final String MESSAGE_ATTRIBUTE = ErrorPage.class.getName() + ".message";

    private ErrorPage() {}

    public static void forward(HttpServletRequest request, HttpServletResponse response, int status, String message)
            throws IOException {
        request.setAttribute(MESSAGE_ATTRIBUTE, message);
        response.sendError(status);
    }

    static Optional<String> message(HttpServletRequest request) {
        return request.getAttribute(MESSAGE_ATTRIBUTE) instanceof String message
                ? Optional.of(message)
                : Optional.empty();
    }
}
