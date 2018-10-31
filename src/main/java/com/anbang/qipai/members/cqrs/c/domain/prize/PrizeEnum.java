package com.anbang.qipai.members.cqrs.c.domain.prize;

public enum PrizeEnum {
    ONE_DAY_MEMBER_CARD("会员天卡*1", 1),
    GOLD_500("玉石*500", 500),
    TWO_DAY_MEMBER_CARD("会员天卡*2", 2),
    GOLD_1000("玉石*1000", 1000),
    GOLD_2000("玉石*2000", 2000);

    private final int num;
    private final String desc;

    PrizeEnum(String desc, int num) {
        this.desc = desc;
        this.num = num;
    }

    public String getDesc() {
        return desc;
    }

    public int getNum() {
        return num;
    }

    public static boolean isGoldType(PrizeEnum prize) {
        return prize == GOLD_500 || prize == GOLD_1000 || prize == GOLD_2000;
    }

    public static boolean isMemberCardType(PrizeEnum prize) {
        return prize == ONE_DAY_MEMBER_CARD || prize == TWO_DAY_MEMBER_CARD;
    }

}
