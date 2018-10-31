package com.anbang.qipai.members.cqrs.c.domain.sign;

import org.springframework.context.ApplicationEvent;


public class SignEvent extends ApplicationEvent {


    public SignEvent(Object source) {
        super(source);
    }
}
