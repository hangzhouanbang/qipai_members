package com.anbang.qipai.members.web.vo;

import com.anbang.qipai.members.cqrs.c.domain.prize.LotteryTypeEnum;

/**
 * @Author: 吴硕涵
 * @Date: 2018/12/21 2:11 PM
 * @Version 1.0
 */
public class LotteryAdviceVO {

    private String id;
    private String name;
    private String icon;
    private String type;
    private String cardType;
    private String singleNum;
    private long stock;
    private boolean overStep;
    private boolean isExtra;
    private String memberId;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getSingleNum() {
        return singleNum;
    }

    public void setSingleNum(String singleNum) {
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

    public boolean isExtra() {
        return isExtra;
    }

    public void setExtra(boolean extra) {
        isExtra = extra;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
