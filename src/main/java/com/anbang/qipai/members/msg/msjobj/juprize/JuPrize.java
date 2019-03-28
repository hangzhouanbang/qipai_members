package com.anbang.qipai.members.msg.msjobj.juprize;

import java.util.Objects;

/**
 * @Description: 对局奖励物品
 */
public class JuPrize {
    private String id;
    private String name;
    private JuPrizeTypeEnum prizeType;//奖品类型
    private String iconUrl;

    private int singleNum;//单奖数量
    private int storeNum;//库存数量
    private int prizeProb;//中奖概率
    private boolean overstep;//超出奖池

    private DrawTypeEnum drawType;  // 抽奖行为本身的类型

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

    public JuPrizeTypeEnum getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(JuPrizeTypeEnum prizeType) {
        this.prizeType = prizeType;
    }

    public int getSingleNum() {
        return singleNum;
    }

    public void setSingleNum(int singleNum) {
        this.singleNum = singleNum;
    }

    public int getStoreNum() {
        return storeNum;
    }

    public void setStoreNum(int storeNum) {
        this.storeNum = storeNum;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getPrizeProb() {
        return prizeProb;
    }

    public void setPrizeProb(int prizeProb) {
        this.prizeProb = prizeProb;
    }

    public boolean isOverstep() {
        return overstep;
    }

    public void setOverstep(boolean overstep) {
        this.overstep = overstep;
    }

    public DrawTypeEnum getDrawType() {
        return drawType;
    }

    public void setDrawType(DrawTypeEnum drawType) {
        this.drawType = drawType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JuPrize juPrize = (JuPrize) o;
        return singleNum == juPrize.singleNum &&
                storeNum == juPrize.storeNum &&
                prizeProb == juPrize.prizeProb &&
                overstep == juPrize.overstep &&
                Objects.equals(id, juPrize.id) &&
                Objects.equals(name, juPrize.name) &&
                prizeType == juPrize.prizeType &&
                Objects.equals(iconUrl, juPrize.iconUrl) &&
                drawType == juPrize.drawType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, prizeType, singleNum, storeNum, iconUrl, prizeProb, overstep, drawType);
    }
}
