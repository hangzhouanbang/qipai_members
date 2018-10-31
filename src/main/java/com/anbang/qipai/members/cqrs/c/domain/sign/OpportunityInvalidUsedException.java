package com.anbang.qipai.members.cqrs.c.domain.sign;

public class OpportunityInvalidUsedException extends Exception{
    public OpportunityInvalidUsedException() {
    }

    public OpportunityInvalidUsedException(String message) {
        super(message);
    }

    public OpportunityInvalidUsedException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpportunityInvalidUsedException(Throwable cause) {
        super(cause);
    }

    public OpportunityInvalidUsedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
