package com.anbang.qipai.members.plan.dao;

import com.anbang.qipai.members.plan.bean.MemberVerifyPhone;

public interface MemberVerifyPhoneDao {
	void save(MemberVerifyPhone memberVerifyPhone);

	MemberVerifyPhone findById(String memberId);
}
