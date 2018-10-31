package com.anbang.qipai.members.cqrs.c.domain.prize;

public class ExchangeException extends Exception {
    public ExchangeException() {
    }

    public ExchangeException(String message) {
        super(message);
    }

    public ExchangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExchangeException(Throwable cause) {
        super(cause);
    }

    public ExchangeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
