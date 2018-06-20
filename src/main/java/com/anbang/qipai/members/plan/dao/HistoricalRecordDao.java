package com.anbang.qipai.members.plan.dao;

import java.util.List;

import com.anbang.qipai.members.plan.domain.historicalrecord.MemberHistoricalRecord;

public interface HistoricalRecordDao {

	void addrecord(MemberHistoricalRecord memberHistoricalRecord);
	
	List<MemberHistoricalRecord> findallrecord(String memberid);
	
	MemberHistoricalRecord findonerecord(String id);
	
}
