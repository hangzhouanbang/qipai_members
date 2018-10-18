package com.anbang.qipai.members.plan.dao;

import com.anbang.qipai.members.plan.bean.MemberLoginLimitRecord;

public interface MemberLoginLimitRecordDao {

	void save(MemberLoginLimitRecord record);

	MemberLoginLimitRecord findByMemberId(String memberId, boolean efficient);

	void updateMemberLoginLimitRecordEfficientById(String[] ids, boolean efficient);

}
