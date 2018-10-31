package com.anbang.qipai.members.cqrs.c.domain.vip;

public enum VIPEnum {

    VIP1(1), VIP2(2), VIP3(3), VIP4(4), VIP5(5);

    private int level;

    VIPEnum(int level) {
        this.level = level;
    }

    public static VIPEnum of(int level) {
        switch (level) {
            case 1:
                return VIP1;
            case 2:
                return VIP2;
            case 3:
                return VIP3;
            case 4:
                return VIP4;
            case 5:
                return VIP5;
            default:
                return null;
        }
    }

    public int getLevel() {
        return level;
    }

}
