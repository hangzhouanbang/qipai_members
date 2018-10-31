package com.anbang.qipai.members.plan.bean;

import org.springframework.data.annotation.Id;

public class Phone {
    @Id
    private String memberId;

    private String phone;

    public Phone() {
    }

    public Phone(String memberId, String phone) {
        this.memberId = memberId;
        this.phone = phone;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
