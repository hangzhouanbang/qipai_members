package com.anbang.qipai.members.cqrs.c.domain.phonefee;

public class MemberPhoneFeeAccountExistsException extends Exception {
    public MemberPhoneFeeAccountExistsException() {
    }

    public MemberPhoneFeeAccountExistsException(String message) {
        super(message);
    }

    public MemberPhoneFeeAccountExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberPhoneFeeAccountExistsException(Throwable cause) {
        super(cause);
    }

    public MemberPhoneFeeAccountExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
