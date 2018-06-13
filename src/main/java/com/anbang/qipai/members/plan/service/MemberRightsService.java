package com.anbang.qipai.members.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.plan.dao.MemberRightsDao;
import com.anbang.qipai.members.plan.domain.MemberRights;


@Component
public class MemberRightsService {
	@Autowired
	private MemberRightsDao memberShipConfigurationDao;

	public void savePlanShipConfiguration(Integer signGoldNumber,Integer goldForNewNember,Integer shareIntegralNumber,Integer shareGoldNumber,
			Integer inviteIntegralNumber,float planGrowIntegralSpeed) {
		MemberRights create = new MemberRights();
		create.setId("1");
		create.setSignGoldNumber(signGoldNumber);
		create.setGoldForNewNember(goldForNewNember);
		create.setShareIntegralNumber(shareIntegralNumber);
		create.setShareGoldNumber(shareGoldNumber);
		create.setInviteIntegralNumber(inviteIntegralNumber);
		create.setPlanGrowIntegralSpeed(planGrowIntegralSpeed);
		memberShipConfigurationDao.savePlanShipConfiguration(create);
	}

	public MemberRights findMemberCreateMemberConfiguration() {
		return memberShipConfigurationDao.find();
	}
	
	public void saveVipShipConfiguration(Integer signGoldNumber,Integer shareIntegralNumber,Integer shareGoldNumber,
			Integer inviteIntegralNumber,float vipGrowIntegralSpeed,float vipGrowGradeSpeed) {
		MemberRights create = new MemberRights();
		create.setId("1");
		create.setSignGoldNumber(signGoldNumber);
		create.setShareIntegralNumber(shareIntegralNumber);
		create.setShareGoldNumber(shareGoldNumber);
		create.setInviteIntegralNumber(inviteIntegralNumber);
		create.setVipGrowIntegralSpeed(vipGrowIntegralSpeed);
		create .setVipGrowGradeSpeed(vipGrowGradeSpeed);
		memberShipConfigurationDao.savePlanShipConfiguration(create);
	}

}
