package com.anbang.qipai.members.cqrs.c.domain.prize;

public class StockInsufficientException extends Exception{

    public StockInsufficientException() {
    }

    public StockInsufficientException(String message) {
        super(message);
    }

    public StockInsufficientException(String message, Throwable cause) {
        super(message, cause);
    }

    public StockInsufficientException(Throwable cause) {
        super(cause);
    }

    public StockInsufficientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
