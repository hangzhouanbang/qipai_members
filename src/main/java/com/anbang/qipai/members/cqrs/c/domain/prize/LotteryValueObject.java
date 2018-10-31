package com.anbang.qipai.members.cqrs.c.domain.prize;

public class LotteryValueObject {

    private final String id;
    private final String name;
    private final int prop;
    private final int firstProp;
    private final LotteryTypeEnum type;
    /**
     * 单次奖品数量
     */
    private final int singleNum;
    /**
     * 库存量
     */
    private int stock;
    /**
     * 是否是超额奖池
     */
    private final boolean overStep;

    public LotteryValueObject(Lottery lottery) {
        this.id = lottery.getId();
        this.name = lottery.getName();
        this.prop = lottery.getProp();
        this.firstProp = lottery.getFirstProp();
        this.type = lottery.getType();
        this.singleNum = lottery.getSingleNum();
        this.overStep = lottery.isOverStep();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getProp() {
        return prop;
    }

    public int getFirstProp() {
        return firstProp;
    }

    public LotteryTypeEnum getType() {
        return type;
    }

    public int getSingleNum() {
        return singleNum;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isOverStep() {
        return overStep;
    }


}
