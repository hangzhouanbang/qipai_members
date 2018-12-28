package com.anbang.qipai.members.web.vo;

/**
 * @Author: 吴硕涵
 * @Date: 2018/12/27 10:12 AM
 * @Version 1.0
 */
public class ExchangeFeeVO {
    private String memberId;
    private String phone;
    private int currency;
    private String exchange;

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

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
}
