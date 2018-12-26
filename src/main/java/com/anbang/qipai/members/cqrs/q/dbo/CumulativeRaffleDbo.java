package com.anbang.qipai.members.cqrs.q.dbo;

import java.util.List;

/**
 * @Author: 吴硕涵
 * @Date: 2018/12/24 2:58 PM
 * @Version 1.0
 */
public class CumulativeRaffleDbo {
    private String id;
    private String time; //以2018-12的形式
    private String memberId;
    private Integer cumulativeDay;
    private Long lastRaffleDay;
    List<LotteryDbo> extraRecordList;//额外奖励记录
    List<Integer> hasRewarded; //已经领取过的奖励

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Integer getCumulativeDay() {
        return cumulativeDay;
    }

    public void setCumulativeDay(Integer cumulativeDay) {
        this.cumulativeDay = cumulativeDay;
    }

    public Long getLastRaffleDay() {
        return lastRaffleDay;
    }

    public void setLastRaffleDay(Long lastRaffleDay) {
        this.lastRaffleDay = lastRaffleDay;
    }

    public List<LotteryDbo> getExtraRecordList() {
        return extraRecordList;
    }

    public void setExtraRecordList(List<LotteryDbo> extraRecordList) {
        this.extraRecordList = extraRecordList;
    }

    public List<Integer> getHasRewarded() {
        return hasRewarded;
    }

    public void setHasRewarded(List<Integer> hasRewarded) {
        this.hasRewarded = hasRewarded;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
