package com.anbang.qipai.members.cqrs.q.dbo;

import com.anbang.qipai.members.cqrs.c.domain.prize.LotteryTypeEnum;

public class LotteryDbo {
    private String id;
    private String name;
    private int prop;
    private int firstProp;
    private String icon;
    private LotteryTypeEnum type;
    private String cardType;
    private int singleNum;
    private long stock;
    private boolean overStep;
    private String index;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProp() {
        return prop;
    }

    public void setProp(int prop) {
        this.prop = prop;
    }

    public int getFirstProp() {
        return firstProp;
    }

    public void setFirstProp(int firstProp) {
        this.firstProp = firstProp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public LotteryTypeEnum getType() {
        return type;
    }

    public void setType(LotteryTypeEnum type) {
        this.type = type;
    }

    public int getSingleNum() {
        return singleNum;
    }

    public void setSingleNum(int singleNum) {
        this.singleNum = singleNum;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public boolean isOverStep() {
        return overStep;
    }

    public void setOverStep(boolean overStep) {
        this.overStep = overStep;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    @Override
    public String toString() {
        return "LotteryDbo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", prop=" + prop +
                ", firstProp=" + firstProp +
                ", icon='" + icon + '\'' +
                ", type=" + type +
                ", cardType='" + cardType + '\'' +
                ", singleNum=" + singleNum +
                ", stock=" + stock +
                ", overStep=" + overStep +
                '}';
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
