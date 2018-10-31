package com.anbang.qipai.members.cqrs.c.domain.prize;

public enum VipPrizeEnum {


    GIFT_SCORE_10("礼券*10", 10), GIFT_SCORE_20("礼券*20", 20), GIFT_SCORE_30("礼券*30", 30), GIFT_SCORE_40("礼券*50", 50), GIFT_SCORE_50("礼券*70", 70);


    private String desc;
    private int score;

    VipPrizeEnum(String desc, int score) {
        this.desc = desc;
        this.score = score;
    }

    public String getDesc() {
        return desc;
    }

    public int getScore() {
        return score;
    }
}
