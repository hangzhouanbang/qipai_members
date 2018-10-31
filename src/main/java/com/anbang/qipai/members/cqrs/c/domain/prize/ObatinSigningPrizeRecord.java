package com.anbang.qipai.members.cqrs.c.domain.prize;

import com.dml.accounting.AccountingRecord;

public class ObatinSigningPrizeRecord {

    private String id;
    private String memberId;
    private PrizeEnum prize;
    private long obtainTime;
    /**
     * 会员等级
     */
    private int vipLevel;
    /**
     * 如果是会员获取的礼券数
     */
    private int score;
    /**
     * 如果是金币奖励
     */
    private AccountingRecord goldAccountingRecord;

    /**
     * 如果是会员
     */
    private AccountingRecord scoreAccountingRecord;

    public ObatinSigningPrizeRecord() {
    }

    public ObatinSigningPrizeRecord(String memberId, PrizeEnum prize, long obtainTime, int vipLevel, int score) {
        this.id = id;
        this.memberId = memberId;
        this.prize = prize;
        this.obtainTime = obtainTime;
        this.vipLevel = vipLevel;
        this.score = score;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setPrize(PrizeEnum prize) {
        this.prize = prize;
    }

    public void setObtainTime(long obtainTime) {
        this.obtainTime = obtainTime;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getMemberId() {
        return memberId;
    }

    public PrizeEnum getPrize() {
        return prize;
    }

    public long getObtainTime() {
        return obtainTime;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public int getScore() {
        return score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AccountingRecord getGoldAccountingRecord() {
        return goldAccountingRecord;
    }

    public void setGoldAccountingRecord(AccountingRecord goldAccountingRecord) {
        this.goldAccountingRecord = goldAccountingRecord;
    }

    public AccountingRecord getScoreAccountingRecord() {
        return scoreAccountingRecord;
    }

    public void setScoreAccountingRecord(AccountingRecord scoreAccountingRecord) {
        this.scoreAccountingRecord = scoreAccountingRecord;
    }

    @Override
    public String toString() {
        return "ObatinSigningPrizeRecord{" +
                "id='" + id + '\'' +
                ", memberId='" + memberId + '\'' +
                ", prize=" + prize +
                ", obtainTime=" + obtainTime +
                ", vipLevel=" + vipLevel +
                ", score=" + score +
                ", goldAccountingRecord=" + goldAccountingRecord +
                ", scoreAccountingRecord=" + scoreAccountingRecord +
                '}';
    }
}
