package com.anbang.qipai.members.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.plan.dao.MemberRightsDao;
import com.anbang.qipai.members.plan.domain.MemberRights;


@Component
public class MemberRightsService {
	@Autowired
	private MemberRightsDao memberShipConfigurationDao;

	public void savePlanShipConfiguration(int signGoldNumber,int goldForNewNember,
			int inviteIntegralNumber,float planGrowIntegralSpeed) {
		MemberRights memberRights = memberShipConfigurationDao.find();
		if(memberRights == null) {
			MemberRights create = new MemberRights();
			create.setId("1");
			create.setSignGoldNumber(signGoldNumber);
			create.setGoldForNewNember(goldForNewNember);
			create.setInviteIntegralNumber(inviteIntegralNumber);
			create.setPlanGrowIntegralSpeed(planGrowIntegralSpeed);
			memberShipConfigurationDao.savePlanShipConfiguration(create);
		}else {
			memberShipConfigurationDao.setPlanMembersRights(planGrowIntegralSpeed, goldForNewNember);
		}
		memberShipConfigurationDao.updatePlanMembersRights(signGoldNumber,goldForNewNember,inviteIntegralNumber,planGrowIntegralSpeed);

	}

	public MemberRights findMemberCreateMemberConfiguration() {
		return memberShipConfigurationDao.find();
	}
	
	public void saveVipShipConfiguration(int signGoldNumber,int inviteIntegralNumber,float vipGrowIntegralSpeed,float vipGrowGradeSpeed) {
		MemberRights memberRights = memberShipConfigurationDao.find();
		if(memberRights == null) {
		MemberRights create = new MemberRights();
		create.setId("1");
		create.setSignGoldNumber(signGoldNumber);
		create.setInviteIntegralNumber(inviteIntegralNumber);
		create.setVipGrowIntegralSpeed(vipGrowIntegralSpeed);
		create .setVipGrowGradeSpeed(vipGrowGradeSpeed);
		memberShipConfigurationDao.savePlanShipConfiguration(create);
		}else {
			memberShipConfigurationDao.setVipMembersRights(vipGrowGradeSpeed);
		}
		memberShipConfigurationDao.updateVipMembersRights(signGoldNumber, inviteIntegralNumber,vipGrowIntegralSpeed,vipGrowGradeSpeed );
	}

}
