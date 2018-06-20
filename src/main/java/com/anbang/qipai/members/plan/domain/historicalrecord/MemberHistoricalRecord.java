package com.anbang.qipai.members.plan.domain.historicalrecord;

import java.util.List;

public class MemberHistoricalRecord {

	private String id;
	
	private String memberid;//会员id
	
	private String wanfa;//玩法id
	
	private long endtime;//结束时间
	
	private List<HistoricalRecord> records;//多个用户战绩信息

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

	public String getWanfa() {
		return wanfa;
	}

	public void setWanfa(String wanfa) {
		this.wanfa = wanfa;
	}
	
	public List<HistoricalRecord> getRecords() {
		return records;
	}

	public void setRecords(List<HistoricalRecord> records) {
		this.records = records;
	}

	public long getEndtime() {
		return endtime;
	}

	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}

	
	
}
