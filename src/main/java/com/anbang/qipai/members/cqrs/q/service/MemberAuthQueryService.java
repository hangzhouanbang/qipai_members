package com.anbang.qipai.members.cqrs.q.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.q.dao.AuthorizationDboDao;
import com.anbang.qipai.members.cqrs.q.dao.MemberDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.AuthorizationDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberRights;
import com.anbang.qipai.members.plan.domain.MemberRightsConfiguration;

@Component
public class MemberAuthQueryService {

	@Autowired
	private AuthorizationDboDao authorizationDboDao;

	@Autowired
	private MemberDboDao memberDboDao;

	public AuthorizationDbo findThirdAuthorizationDbo(String publisher, String uuid) {
		return authorizationDboDao.find(true, publisher, uuid);
	}

	public void updateMember(String memberId, String nickname, String headimgurl) {
		memberDboDao.update(memberId, nickname, headimgurl);
	}

	public void updateLoginTime(String memberId, long loginTime) {
		memberDboDao.updateLoginTime(memberId, loginTime);
	}

	public void createMemberAndAddThirdAuth(String memberId, String publisher, String uuid,
			MemberRightsConfiguration memberRightsConfiguration) {
		MemberDbo memberDbo = new MemberDbo();
		memberDbo.setId(memberId);
		memberDbo.setVip(false);
		memberDbo.setCost(0);
		memberDbo.setVipLevel(0);
		memberDbo.setVipScore(0);
		memberDbo.setLastLoginTime(System.currentTimeMillis());
		memberDbo.setCreateTime(System.currentTimeMillis());
		memberDbo.setRights(memberRightsConfiguration.generateRightsForPlanMembers());
		memberDboDao.save(memberDbo);

		AuthorizationDbo authDbo = new AuthorizationDbo();
		authDbo.setMemberId(memberId);
		authDbo.setPublisher(publisher);
		authDbo.setThirdAuth(true);
		authDbo.setUuid(uuid);
		authorizationDboDao.save(authDbo);
	}

	public void addThirdAuth(String publisher, String uuid, String memberId) {
		AuthorizationDbo authDbo = new AuthorizationDbo();
		authDbo.setMemberId(memberId);
		authDbo.setPublisher(publisher);
		authDbo.setThirdAuth(true);
		authDbo.setUuid(uuid);
		authorizationDboDao.save(authDbo);
	}

	public MemberDbo findMember(String memberId) {
		return memberDboDao.findById(memberId);
	}

	public void updatePlanMembersRights(MemberRights memberRights) {
		memberDboDao.updatePlanMembersRights(memberRights);
	}

	public void updateVipMembersRights(MemberRights memberRights) {
		memberDboDao.updateVipMembersRights(memberRights);
	}

}
