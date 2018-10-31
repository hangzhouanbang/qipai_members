package com.anbang.qipai.members.plan.bean;

import org.springframework.data.annotation.Id;

public class Address {
    @Id
    private String memberId;
    private String name;
    private String phone;
    private String address;

    public Address() {
    }

    public Address(String memberId, String name, String phone, String address) {
        this.memberId = memberId;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
