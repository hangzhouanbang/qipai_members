package com.anbang.qipai.members.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.plan.dao.MemberDao;
import com.anbang.qipai.members.plan.domain.Member;

@Service
public class MemberService {

	@Autowired
	private MemberDao memberDao;

	public Member findMember(String memberId) {
		Query query = new Query(Criteria.where("id").is(memberId));
		return memberDao.findMember(query);
	}

	public void registerPhone(String memberId, String phone) {
		Query query = new Query(Criteria.where("id").is(memberId));
		Update update = new Update();
		update.addToSet("phone", phone);
		memberDao.updateMember(query, update);
	}
}
