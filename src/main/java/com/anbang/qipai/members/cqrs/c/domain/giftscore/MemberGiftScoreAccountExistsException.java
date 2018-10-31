package com.anbang.qipai.members.cqrs.c.domain.giftscore;

public class MemberGiftScoreAccountExistsException extends Exception {
    public MemberGiftScoreAccountExistsException() {
    }

    public MemberGiftScoreAccountExistsException(String message) {
        super(message);
    }

    public MemberGiftScoreAccountExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberGiftScoreAccountExistsException(Throwable cause) {
        super(cause);
    }

    public MemberGiftScoreAccountExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
