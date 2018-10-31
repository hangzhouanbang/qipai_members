package com.anbang.qipai.members.web.vo;

import com.anbang.qipai.members.cqrs.c.domain.prize.ObatinSigningPrizeRecord;

public class ObtainSigningPrizeRecordVo {
    private String id;
    private String memberId;
    private String prizeName;
    private long time;
    private int vipLevel;
    private int score;

    public ObtainSigningPrizeRecordVo(ObatinSigningPrizeRecord record) {
        this.id = record.getId();
        this.memberId = record.getMemberId();
        this.prizeName = record.getPrize().getDesc();
        this.vipLevel = record.getVipLevel();
        this.score = record.getScore();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
