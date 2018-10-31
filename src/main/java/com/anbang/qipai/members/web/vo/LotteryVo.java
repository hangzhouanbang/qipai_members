package com.anbang.qipai.members.web.vo;

public class LotteryVo {

    private String id;

    private String icon;

    private String name;

    public LotteryVo(String id, String icon, String name) {
        this.id = id;
        this.icon = icon;
        this.name = name;
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

}
