package com.anbang.qipai.members.web.enums;

/**
 * @Author: 吴硕涵
 * @Date: 2018/12/25 11:21 AM
 * @Version 1.0
 */
public enum ExtraRewardDay {
    REWARD1("3day"),
    REWARD2("5day"),
    REWARD3("7day"),
    REWARD4("15day"),
    REWARD5("25day")
    ;


    String date;

    ExtraRewardDay(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
