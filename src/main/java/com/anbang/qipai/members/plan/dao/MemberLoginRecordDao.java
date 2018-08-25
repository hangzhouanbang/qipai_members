package com.anbang.qipai.members.plan.dao;

import com.anbang.qipai.members.plan.bean.MemberLoginRecord;

public interface MemberLoginRecordDao {

	void save(MemberLoginRecord record);

	void updateOnlineTimeById(String id, long onlineTime);

	MemberLoginRecord findRecentRecordByMemberId(String memberId);
}
