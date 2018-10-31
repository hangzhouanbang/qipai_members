package com.anbang.qipai.members.cqrs.c.domain.sign;

public class NotSignedException extends Exception {
    public NotSignedException() {
    }

    public NotSignedException(String message) {
        super(message);
    }

    public NotSignedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSignedException(Throwable cause) {
        super(cause);
    }

    public NotSignedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
