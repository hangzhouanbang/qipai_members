package com.anbang.qipai.members.cqrs.q.dao;

import com.anbang.qipai.members.cqrs.q.dbo.MemberRechargeRecordDbo;

public interface MemberRechargeRecordDboDao {
	
	 MemberRechargeRecordDbo find_grade(String memberId);
	 
	 void saveMemberRechargeAmount(MemberRechargeRecordDbo memberRechargeRecordDbo);

}