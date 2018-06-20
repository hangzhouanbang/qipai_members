package com.anbang.qipai.members.plan.domain.historicalrecord;

import java.util.Comparator;

public class HistoricalRecord implements Comparator<HistoricalRecord>{
	
	private String id;
	
	private String memberid;//用户编号
	
	private String nickname;//会员名称
	
	private String roomid;//房间编号
	
	private int hucount;//胡几次
	
	private int paocount;//多少炮
	
	private int maxhucount;//最大胡几次
	
	private int totalscore;//总分
	
	private long endtime;//结束时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMemberid() {
		return memberid;
	}

	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}

	public String getRoomid() {
		return roomid;
	}

	public void setRoomid(String roomid) {
		this.roomid = roomid;
	}
	
	public int getHucount() {
		return hucount;
	}

	public void setHucount(int hucount) {
		this.hucount = hucount;
	}

	public int getPaocount() {
		return paocount;
	}

	public void setPaocount(int paocount) {
		this.paocount = paocount;
	}

	public int getMaxhucount() {
		return maxhucount;
	}

	public void setMaxhucount(int maxhucount) {
		this.maxhucount = maxhucount;
	}

	public int getTotalscore() {
		return totalscore;
	}

	public void setTotalscore(int totalscore) {
		this.totalscore = totalscore;
	}

	public long getEndtime() {
		return endtime;
	}

	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	@Override
	public int compare(HistoricalRecord o1, HistoricalRecord o2) {
		if(o1.getTotalscore() > o2.getTotalscore()) {
			return 1;
		}else if(o1.getTotalscore() == o2.getTotalscore()){
			return 0;
		}else {
			return -1;
		}
	}
	
	

}
