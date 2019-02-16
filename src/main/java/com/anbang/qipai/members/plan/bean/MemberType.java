package com.anbang.qipai.members.plan.bean;

/**
 * @Description: 当前会员类型
 */
public class MemberType {
    private String id;
    private String cardType;  //最近一次冲卡的类型
    private CardSouceEnum cardSource;  //卡的来源
    private long vipEndTime;   //vip到期时间（保留字段）
    private boolean isPay;  //是否付费会员
    private boolean isValid;  //是否有效

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public CardSouceEnum getCardSource() {
        return cardSource;
    }

    public void setCardSource(CardSouceEnum cardSource) {
        this.cardSource = cardSource;
    }

    public long getVipEndTime() {
        return vipEndTime;
    }

    public void setVipEndTime(long vipEndTime) {
        this.vipEndTime = vipEndTime;
    }

    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
