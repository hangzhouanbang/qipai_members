package com.anbang.qipai.members.cqrs.c.domain.hongbao;

public class MemberHongBaoAccountAlreadException extends Exception {
    public MemberHongBaoAccountAlreadException() {
    }

    public MemberHongBaoAccountAlreadException(String message) {
        super(message);
    }

    public MemberHongBaoAccountAlreadException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberHongBaoAccountAlreadException(Throwable cause) {
        super(cause);
    }

    public MemberHongBaoAccountAlreadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
