package com.anbang.qipai.members.web.vo;

public class RaffleHistoryVO {
    private String id;
    private String memberId;
    private String lotteryId;
    private String lotteryName;
    private String type;
    private long time;
    private boolean isFirst;

    public RaffleHistoryVO(){}

    public RaffleHistoryVO(String id, String memberId, String lotteryId, String lotteryName, String type, long time, boolean isFirst) {
        this.id = id;
        this.memberId = memberId;
        this.lotteryId = lotteryId;
        this.lotteryName = lotteryName;
        this.type = type;
        this.time = time;
        this.isFirst = isFirst;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getLotteryName() {
        return lotteryName;
    }

    public void setLotteryName(String lotteryName) {
        this.lotteryName = lotteryName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
