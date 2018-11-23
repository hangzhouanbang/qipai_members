package com.anbang.qipai.members.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.plan.bean.MemberVerifyPhone;
import com.anbang.qipai.members.plan.dao.MemberVerifyPhoneDao;

@Service
public class MemberVerifyPhoneService {
	@Autowired
	private MemberVerifyPhoneDao memberVerifyPhoneDao;

	public void save(MemberVerifyPhone memberVerifyPhone) {
		memberVerifyPhoneDao.save(memberVerifyPhone);
	}

	public MemberVerifyPhone findById(String memberId) {
		return memberVerifyPhoneDao.findById(memberId);
	}
}
