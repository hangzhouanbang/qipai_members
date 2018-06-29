package com.anbang.qipai.members.plan.dao;

import java.util.List;

import com.anbang.qipai.members.plan.domain.historicalrecord.MemberHistoricalRecord;

public interface HistoricalRecordDao {

	void addRecord(MemberHistoricalRecord memberHistoricalRecord);
	
	List<MemberHistoricalRecord> findAllRecord(String memberid);
	
	MemberHistoricalRecord findOneRecord(String id);
	
}
