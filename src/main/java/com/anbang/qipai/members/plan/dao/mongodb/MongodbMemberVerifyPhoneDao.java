package com.anbang.qipai.members.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.plan.bean.MemberVerifyPhone;
import com.anbang.qipai.members.plan.dao.MemberVerifyPhoneDao;
import com.anbang.qipai.members.plan.dao.mongodb.repository.MemberVerifyPhoneRepository;

@Component
public class MongodbMemberVerifyPhoneDao implements MemberVerifyPhoneDao {

	@Autowired
	private MemberVerifyPhoneRepository memberVerifyPhoneRepository;

	@Override
	public void save(MemberVerifyPhone memberVerifyPhone) {
		memberVerifyPhoneRepository.save(memberVerifyPhone);
	}

	@Override
	public MemberVerifyPhone findById(String memberId) {
		return memberVerifyPhoneRepository.findOne(memberId);
	}

}
