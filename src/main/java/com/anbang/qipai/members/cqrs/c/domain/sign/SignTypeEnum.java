package com.anbang.qipai.members.cqrs.c.domain.sign;

public enum SignTypeEnum {
    THREE(3, "三天"),
    FIVE(5, "五天"),
    SEVEN(7, "七天"),
    FIFTEEN(15, "十五天"),
    TWENTY(20, "二十天");


    private int day;
    private String desc;

    SignTypeEnum(int day, String desc) {
        this.day = day;
        this.desc = desc;
    }

    public static SignTypeEnum of(int day) {
        for (SignTypeEnum type : SignTypeEnum.values()) {
            if (type.day == day)
                return type;
        }
        return null;
    }

}
