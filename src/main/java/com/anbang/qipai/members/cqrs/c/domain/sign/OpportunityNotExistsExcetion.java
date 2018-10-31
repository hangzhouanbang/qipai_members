package com.anbang.qipai.members.cqrs.c.domain.sign;

public class OpportunityNotExistsExcetion extends Exception {
    public OpportunityNotExistsExcetion() {
    }

    public OpportunityNotExistsExcetion(String message) {
        super(message);
    }

    public OpportunityNotExistsExcetion(String message, Throwable cause) {
        super(message, cause);
    }

    public OpportunityNotExistsExcetion(Throwable cause) {
        super(cause);
    }

    public OpportunityNotExistsExcetion(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
