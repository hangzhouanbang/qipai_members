package com.anbang.qipai.members.msg.msjobj.juprize;


/**
 * @Description: 对局奖励记录
 */
public class JuPrizeRecord {
    private String id;
    private String playerId;
    private long sendTime;
    private JuPrize juPrize;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public JuPrize getJuPrize() {
        return juPrize;
    }

    public void setJuPrize(JuPrize juPrize) {
        this.juPrize = juPrize;
    }
}
