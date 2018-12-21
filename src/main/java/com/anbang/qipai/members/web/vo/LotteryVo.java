package com.anbang.qipai.members.web.vo;

public class LotteryVo {

    private String id;

    private String icon;

    private String name;

    private String type;

    private String cardType;

    public LotteryVo(String id, String icon, String name, String type, String cardType) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.type = type;
        this.cardType = cardType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
